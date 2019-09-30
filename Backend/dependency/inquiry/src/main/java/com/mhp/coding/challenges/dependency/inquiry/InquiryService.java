package com.mhp.coding.challenges.dependency.inquiry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public class InquiryService {

	private static final String EMAIL_HANDLER = "emailHandler";

	private static final String PUSH_NOTIFICATION_HANDLER = "pushNotificationHandler";

	private static final Logger LOG = LoggerFactory.getLogger(InquiryService.class);

	private ApplicationContext appContext;

	@Autowired
	public InquiryService(ApplicationContext appContext) {
		this.appContext = appContext;
	}

	public void create(final Inquiry inquiry) {
		LOG.info("User sent inquiry: {}", inquiry);

		invokeEmailHandler(inquiry);
		invokePushNotificationHandler(inquiry);

	}

	private void invokeEmailHandler(final Inquiry inquiry) {
		final Object emailHandler = appContext.getBean(EMAIL_HANDLER);
		try {
			final Method method = emailHandler.getClass().getMethod("sendEmail", Inquiry.class);
			method.invoke(emailHandler, inquiry);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			LOG.error(String.format("Invocation of object \'%s\' failed.", EMAIL_HANDLER), e);
		}
	}

	private void invokePushNotificationHandler(final Inquiry inquiry) {
		final Object pushNotifHandler = appContext.getBean(PUSH_NOTIFICATION_HANDLER);
		try {
			final Method method = pushNotifHandler.getClass().getMethod("sendNotification", Inquiry.class);
			method.invoke(pushNotifHandler, inquiry);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException
				| InvocationTargetException e) {
			LOG.error(String.format("Invocation of object \'%s\' failed.", PUSH_NOTIFICATION_HANDLER), e);
		}

	}

}
