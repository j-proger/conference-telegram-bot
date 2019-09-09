package com.jproger.conferencetelegrambot.core.operations.executors;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseOperationExecutor;
import com.jproger.conferencetelegrambot.core.operations.dto.FinishTopicSystemOperation;
import com.jproger.conferencetelegrambot.core.operations.dto.SendTextMessageSystemOperation;
import com.jproger.conferencetelegrambot.topics.TopicService;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import com.jproger.conferencetelegrambot.workflow.entities.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FinishTopicSystemOperationExecutor extends BaseOperationExecutor<FinishTopicSystemOperation> {
    private final TopicService topicService;
    private final UserStateService userStateService;

    public FinishTopicSystemOperationExecutor(ActionBus actionBus,
                                              TopicService topicService,
                                              UserStateService userStateService) {
        super(FinishTopicSystemOperation.class, actionBus);

        this.topicService = topicService;
        this.userStateService = userStateService;
    }

    @Override
    protected void acceptTAction(FinishTopicSystemOperation action) {
        List<UserStateDto> subscribedUserStates = userStateService.getUserStatesSubscribedToTopic(action.getTopicId());

        topicService.finishTopic(action.getTopicId());

        userStateService.clearTopicIdFromSubscribedUsers(action.getTopicId());

        notifyUsersAboutFinishTopic(subscribedUserStates);

        startFeedbackCollecting(subscribedUserStates);
    }

    private void notifyUsersAboutFinishTopic(List<UserStateDto> states) {
        states.forEach(us -> {
            SendTextMessageSystemOperation action = new SendTextMessageSystemOperation(us.getChannel(), us.getChannelUserId(), "Выбранный вами доклад завершен.");

            actionBus.sendAction(action);
        });
    }

    private void startFeedbackCollecting(List<UserStateDto> states) {
        states.stream()
                .filter(us -> userStateService.updateStatus(us.getChannel(), us.getChannelUserId(), Status.COLLECT_FEEDBACK))
                .forEach(us -> {
                    SendTextMessageSystemOperation action = new SendTextMessageSystemOperation(us.getChannel(), us.getChannelUserId(), "Позвольте спросить, как вам моя работа? Напишите мне сообщение (или несколько сообщений) с оценкой моей работы. Что было сделано хорошо? Что не очень? Чего не хавтало?");

                    actionBus.sendAction(action);
                });
    }
}
