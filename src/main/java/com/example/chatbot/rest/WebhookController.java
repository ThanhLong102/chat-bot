//package com.example.chatbot.rest;
//
//import com.example.chatbot.service.ChatBotService;
//import com.github.messenger4j.Messenger;
//import com.github.messenger4j.exception.MessengerApiException;
//import com.github.messenger4j.exception.MessengerIOException;
//import com.github.messenger4j.exception.MessengerVerificationException;
//import com.github.messenger4j.send.MessagePayload;
//import com.github.messenger4j.send.MessagingType;
//import com.github.messenger4j.send.NotificationType;
//import com.github.messenger4j.send.message.TextMessage;
//import com.github.messenger4j.send.recipient.IdRecipient;
//import com.github.messenger4j.webhook.event.TextMessageEvent;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import static com.github.messenger4j.Messenger.*;
//import static java.util.Optional.empty;
//import static java.util.Optional.of;
//
//@RestController
//@CrossOrigin("*")
//@RequestMapping("/webhook")
//public class WebhookController {
//    private static final Logger logger = LoggerFactory.getLogger(WebhookController.class);
//
//    private final Messenger messenger;
//
//    private final ChatBotService chatBotService;
//
//    public WebhookController(final Messenger messenger,  ChatBotService chatBotService) {
//        this.messenger = messenger;
//        this.chatBotService = chatBotService;
//    }
//
//    @GetMapping
//    public ResponseEntity<String> verifyWebhook(@RequestParam(MODE_REQUEST_PARAM_NAME) final String mode,
//                                                @RequestParam(VERIFY_TOKEN_REQUEST_PARAM_NAME) final String verifyToken, @RequestParam(CHALLENGE_REQUEST_PARAM_NAME) final String challenge) {
//        logger.debug("Received Webhook verification request - mode: {} | verifyToken: {} | challenge: {}", mode, verifyToken, challenge);
//        try {
//            this.messenger.verifyWebhook(mode, verifyToken);
//            return ResponseEntity.ok(challenge);
//        } catch (MessengerVerificationException e) {
//            logger.warn("Webhook verification failed: {}", e.getMessage());
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
//        }
//    }
//
//    @PostMapping
//    public ResponseEntity<Void> handleCallback(@RequestBody final String payload, @RequestHeader(SIGNATURE_HEADER_NAME) final String signature) throws MessengerVerificationException {
//        logger.info("payload", payload);
//        logger.info("signature", signature);
//        this.messenger.onReceiveEvents(payload, of(signature), event -> {
//            if (event.isTextMessageEvent()) {
//                try {
//                    logger.info("0");
//                    handleTextMessageEvent(event.asTextMessageEvent());
//                    logger.info("1");
//                } catch (MessengerApiException e) {
//                    logger.info("2");
//                    e.printStackTrace();
//                } catch (MessengerIOException e) {
//                    logger.info("3");
//                    e.printStackTrace();
//                }
//            } else {
//                String senderId = event.senderId();
//                sendTextMessageUser(senderId, "Tôi là bot chỉ có thể xử lý tin nhắn văn bản.");
//            }
//        });
//        return ResponseEntity.status(HttpStatus.OK).build();
//    }
//
//    @PostMapping("/reply")
//    public ResponseEntity<String> handleCustomMessage(@RequestBody String message)  {
//        String reply = chatBotService.sendMessage(message);
//        return ResponseEntity.status(HttpStatus.OK).body(reply);
//    }
//
//    private void handleTextMessageEvent(TextMessageEvent event) throws MessengerApiException, MessengerIOException {
//        final String senderId = event.senderId();
//        final String ans = chatBotService.sendMessage(event.text());
//        sendTextMessageUser(senderId, ans);
//    }
//
//    private void sendTextMessageUser(String idSender, String text) {
//        try {
//            final IdRecipient recipient = IdRecipient.create(idSender);
//            final NotificationType notificationType = NotificationType.REGULAR;
//            final String metadata = "DEVELOPER_DEFINED_METADATA";
//
//            final TextMessage textMessage = TextMessage.create(text, empty(), of(metadata));
//            final MessagePayload messagePayload = MessagePayload.create(recipient, MessagingType.RESPONSE, textMessage,
//                    of(notificationType), empty());
//            this.messenger.send(messagePayload);
//        } catch (MessengerApiException | MessengerIOException e) {
//            handleSendException(e);
//        }
//    }
//
//    private void handleSendException(Exception e) {
//        logger.error("Message could not be sent. An unexpected error occurred.", e);
//    }
//}
