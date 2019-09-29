package com.jproger.conferencetelegrambot.core.operations;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.common.operations.BaseOperation;
import com.jproger.conferencetelegrambot.common.actions.SendTextMessageSystemAction;
import com.jproger.conferencetelegrambot.topics.TopicService;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import com.jproger.conferencetelegrambot.workflow.entities.Status;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FinishTopicUserOperation extends BaseOperation {
    private final TopicService topicService;
    private final UserStateService userStateService;

    public FinishTopicUserOperation(ActionBus actionBus,
                                    TopicService topicService,
                                    UserStateService userStateService) {
        super(actionBus);

        this.topicService = topicService;
        this.userStateService = userStateService;
    }

    public void execute(long topicId) {
        List<UserStateDto> subscribedUserStates = userStateService.getUserStatesSubscribedToTopic(topicId);

        topicService.finishTopic(topicId);

        userStateService.clearTopicIdFromSubscribedUsers(topicId);

        notifyUsersAboutFinishTopic(subscribedUserStates);

        startFeedbackCollecting(subscribedUserStates);
    }

    private void notifyUsersAboutFinishTopic(List<UserStateDto> states) {
        states.forEach(us -> {
            SendTextMessageSystemAction action = new SendTextMessageSystemAction(us.getChannel(), us.getChannelUserId(), "Выбранный вами доклад завершен.");

            actionBus.sendAction(action);
        });
    }

    private void startFeedbackCollecting(List<UserStateDto> states) {
        states.stream()
                .filter(us -> userStateService.updateStatus(us.getChannel(), us.getChannelUserId(), Status.COLLECT_FEEDBACK))
                .forEach(us -> {
                    SendTextMessageSystemAction action = new SendTextMessageSystemAction(us.getChannel(), us.getChannelUserId(), "Позвольте спросить, как вам моя работа? Напишите мне сообщение (или несколько сообщений) с оценкой моей работы. Что было сделано хорошо? Что не очень? Чего не хавтало?");

                    actionBus.sendAction(action);
                });
    }
}
