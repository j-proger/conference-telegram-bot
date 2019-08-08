package com.jproger.conferencetelegrambot.action.consumers;

import com.jproger.conferencetelegrambot.action.bus.ActionBus;
import com.jproger.conferencetelegrambot.action.bus.dto.Action.ChannelType;
import com.jproger.conferencetelegrambot.action.bus.dto.MakeQuestionUserAction;
import com.jproger.conferencetelegrambot.action.bus.dto.RequestContactSystemAction;
import com.jproger.conferencetelegrambot.action.consumers.exceptions.UserActionException;
import com.jproger.conferencetelegrambot.topics.TopicService;
import com.jproger.conferencetelegrambot.users.UserService;
import com.jproger.conferencetelegrambot.users.dto.UserDto;
import com.jproger.conferencetelegrambot.workflow.UserStateService;
import com.jproger.conferencetelegrambot.workflow.dto.UserStateDto;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MakeQuestionUserActionConsumer extends BaseActionConsumer<MakeQuestionUserAction> {
    private final TopicService topicService;
    private final UserService userService;
    private final UserStateService userStateService;

    public MakeQuestionUserActionConsumer(ActionBus actionBus,
                                          TopicService topicService,
                                          UserService userService,
                                          UserStateService userStateService) {
        super(MakeQuestionUserAction.class, actionBus);

        this.topicService = topicService;
        this.userService = userService;
        this.userStateService = userStateService;
    }

    @Override
    protected void acceptTAction(MakeQuestionUserAction action) {
        Optional<UserStateDto> userState = getUserStateByAction(action);
        Optional<UserDto> user = getUserByUserState(userState);

        if (userState.isPresent() && user.isPresent()) {
            createQuestion(userState.get(), user.get(), action.getQuestion());
        } else {
            requestRegisterUser(action.getChannel(), action.getChannelUserId());
        }
    }

    private Optional<UserStateDto> getUserStateByAction(MakeQuestionUserAction action) {
        return userStateService.getUserStateByChannelAndChannelUserId(action.getChannel(), action.getChannelUserId());
    }

    private Optional<UserDto> getUserByUserState(Optional<UserStateDto> userState) {
        return userState.map(UserStateDto::getInnerUserId)
                .flatMap(userService::getUserById);
    }

    private void requestRegisterUser(ChannelType channel, String channelUserId) {
        RequestContactSystemAction action = new RequestContactSystemAction(channel, channelUserId, "You don't registered yet. Please, give us your phone number");

        actionBus.sendAction(action);
    }

    private void createQuestion(UserStateDto userState, UserDto user, String question) {
        topicService.createQuestion(user.getId(), userState.getTopicKey(), question)
                .orElseThrow(() -> new UserActionException("Something went wrong. Probably you didn't select a topic."));
    }
}
