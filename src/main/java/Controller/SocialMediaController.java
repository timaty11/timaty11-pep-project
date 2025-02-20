package Controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import io.javalin.Javalin;
import io.javalin.http.Context;


public class SocialMediaController {
    AccountService accountService;
    MessageService messageService;

    public SocialMediaController() {
        this.accountService = new AccountService();
        this.messageService = new MessageService();
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        // Authorization operations
        app.post("/login", this::loginHandler);
        app.post("/register", this::registerHandler);

        // Messages operations
        app.get("/messages", this::getAllMessagesHandler);  // Get all messages
        app.post("/messages", this::addMessageHandler); // Add message
        app.get("/messages/{message_id}", this::getMessageByIDHandler);     // Retrieve message by ID
        app.delete("/messages/{message_id}", this::deleteMessageHandler);   // Delete message by ID
        app.patch("/messages/{message_id}", this::updateMessageHandler);    // Update message by ID
        app.get("/accounts/{account_id}/messages", this::getAllMessagesByAuthorIDHandler);  // Get all messages from account_id

        app.error(404, this::NotFoundHandler);
        app.exception(Exception.class, this::genericExceptionHandler);
        return app;
    }

    
    private void registerHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(context.body(), Account.class);
        // Basic checkups
        if (account == null || 
            account.getUsername() == null || 
            account.getUsername() == "" || 
            account.getPassword().length() < 4) {
                context.status(400);
                return;
        }
        
        // If everything passes add user to a DB
        Account addedAccount = accountService.addAccount(account);

        // Check if user was added to a DB
        if(addedAccount != null) {
            context.json(mapper.writeValueAsString(addedAccount));
            context.status(200);
        } else {
            context.status(400);
        }
    }

    private void loginHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account enteredData = mapper.readValue(context.body(), Account.class);

        Account account = accountService.checkPasswordMatch(enteredData);
        if (account != null) {
            context.json(mapper.writeValueAsString(account));
            context.status(200);
        } else {
            context.status(401);
        }
    }

    private void getAllMessagesHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        context.json(mapper.writeValueAsString(messageService.getAllMessages()));
        context.status(200);
    }

    private void getMessageByIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageIdParam = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.getMessageByID(messageIdParam);
        
        if (message != null) {
            context.json(mapper.writeValueAsString(message));
        } else {
            context.json("");
        }
        
        context.status(200);
    }

    private void getAllMessagesByAuthorIDHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int authorIdParam = Integer.parseInt(context.pathParam("account_id"));

        context.json(mapper.writeValueAsString(messageService.getMessagesByAutohrID(authorIdParam)));
        context.status(200);
    }

    private void addMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message enteredMessage = mapper.readValue(context.body(), Message.class);

        // Basic checkups
        if (enteredMessage == null || 
            enteredMessage.getMessage_text() == null || 
            enteredMessage.getMessage_text() == "" || 
            enteredMessage.getMessage_text().length() > 255) {
                context.status(400);
                return;
        }

        // If everything passes add user to a DB
        Message message = messageService.addMessage(enteredMessage);

        // Check if message was added to a DB
        if(message != null) {
            context.json(mapper.writeValueAsString(message));
            context.status(200);
        } else {
            context.status(400);
        }
    }

    private void deleteMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int messageIdParam = Integer.parseInt(context.pathParam("message_id"));
        Message message = messageService.deleteMessage(messageIdParam);
        
        if (message != null) {
            context.json(mapper.writeValueAsString(message));
        } else {
            context.json("");
        }
        
        context.status(200);
    }

    private void updateMessageHandler(Context context) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message enteredMessage = mapper.readValue(context.body(), Message.class);
        int messageIdParam = Integer.parseInt(context.pathParam("message_id"));

        // Basic checkups
        if (enteredMessage == null || 
            enteredMessage.getMessage_text() == null || 
            enteredMessage.getMessage_text() == "" || 
            enteredMessage.getMessage_text().length() > 255) {
                context.status(400);
                return;
        }

        // If everything passes update message in a DB
        Message message = messageService.updateMessage(enteredMessage, messageIdParam);

        // Check if message was added to a DB
        if(message != null) {
            context.json(mapper.writeValueAsString(message));
            context.status(200);
        } else {
            context.status(400);
        }
    }


    private void NotFoundHandler(Context ctx) {
        ctx.result("Resource not found.");
    }

    private void genericExceptionHandler(Exception e, Context ctx) {
        ctx.result("Oops, something went wrong: " + e.getClass() + "\n" + e.getMessage());
    }
}