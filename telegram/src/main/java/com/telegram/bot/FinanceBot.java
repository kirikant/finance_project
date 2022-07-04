package com.telegram.bot;

import com.telegram.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.ArrayList;
import java.util.List;

@Component
public class FinanceBot extends TelegramLongPollingBot {


    @Autowired
    private AccountService accountService;
    @Autowired
    private OperationService operationService;
    @Autowired
    private UserService userService;
    @Autowired
    private CrossServiceGetter crossServiceGetter;
    @Autowired
    private ReportService reportService;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery(), update);
        } else if (update.hasMessage()) {
            handleMessage(update);
        }
    }

    private void handleMessage(Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setText("Hi!");
        sendMessage.setChatId(String.valueOf(update.getMessage().getChatId()));
        List<List<InlineKeyboardButton>> buttonList = new ArrayList<>();

        InlineKeyboardMarkup keyboardMarkup = new InlineKeyboardMarkup(buttonList);
        sendMessage.setReplyMarkup(keyboardMarkup);

        if (update.getMessage().getText().contains("1.title:")) {
            accountService.createAccount(update.getMessage().getText(), sendMessage);
        }
        if (update.getMessage().getText().contains("1.description of the operation:")){
          operationService.createOperation(update.getMessage().getText(),sendMessage);
        }
        if (update.getMessage().getText().contains("1.update title:")){
            accountService.updateAccount(update.getMessage().getText(),sendMessage);
        }
        //register
        if (update.getMessage().getText().contains("email:")){
            userService.register(String.valueOf(update.getMessage().getChatId()),
                    update.getMessage().getText(),sendMessage);
        }
        if (update.getMessage().getText().contains("login:")&&
                !update.getMessage().getText().contains("email:")){
            userService.login(String.valueOf(update.getMessage().getChatId()),
                    update.getMessage().getText(),sendMessage);
        }
        if (update.getMessage().getText().contains("3.date to:")) {
            operationService.getOperationsInfo(sendMessage,update.getMessage().getText());
        }
        if (update.getMessage().getText().contains("uuid of operation for update:")) {
            operationService.updateOperationForm(sendMessage,
                    update.getMessage().getText().split(":")[1]);
        }
        if (update.getMessage().getText().contains("1.updated description of the operation:")) {
            operationService.updateOperation(sendMessage,update.getMessage().getText());
        }
        if (update.getMessage().getText().contains("1.operation uuid:")) {
            operationService.deleteOperation(sendMessage,update.getMessage().getText());
        }
        if (update.getMessage().getText().contains("1.type:BY_DATE")) {
            reportService.byDateReport(sendMessage,update.getMessage().getText());
        }
        if (update.getMessage().getText().contains("1.type:BY_CATEGORY")) {
            reportService.byCategoryReport(sendMessage,update.getMessage().getText());
        }
        if (update.getMessage().getText().contains("1.uuids of accounts:")) {
            reportService.balanceReport(sendMessage,update.getMessage().getText());
        }

        if (userService.validate(update.getMessage().getChatId().toString())){
            buttonList.add(List.of(InlineKeyboardButton.builder().text("accounts").callbackData("accounts").build(),
                    InlineKeyboardButton.builder().text("operations").callbackData("operations").build(),
                    InlineKeyboardButton.builder().text("reports").callbackData("reports").build()));
            buttonList.add(List.of(
                    InlineKeyboardButton.builder().text("categories").callbackData("categories").build(),
                    InlineKeyboardButton.builder().text("currencies").callbackData("currencies").build()));
            buttonList.add(List.of(
                    InlineKeyboardButton.builder().text("logout").callbackData("logout,"+
                            update.getMessage().getChatId()).build()));
        } else {
            buttonList.add(List.of(InlineKeyboardButton.builder().text("register").callbackData("register").build(),
                    InlineKeyboardButton.builder().text("login").callbackData("login").build()));
        }
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    private void handleCallback(CallbackQuery callbackQuery, Update update) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(update.getCallbackQuery().getMessage().getChatId()));

        if (callbackQuery.getData().equals("accounts")) {
            accountService.accountsResponse(sendMessage);
        }
        if (callbackQuery.getData().equals("operations")) {
            operationService.operationsResponse(sendMessage);
        }
        //accounts
        if (callbackQuery.getData().equals("add account")) {
            accountService.addAccountForm(sendMessage);
        }
        if (callbackQuery.getData().equals("add operation")) {
            operationService.addOperation(sendMessage);
        }
        if (callbackQuery.getData().equals("get accounts")) {
            accountService.getAccounts(sendMessage,callbackQuery.getData());
        }
        if (callbackQuery.getData().contains("account  uuid:")) {
            accountService.getAccountInfo(sendMessage,callbackQuery.getData());
        }
        if (callbackQuery.getData().equals("accounts.all")) {
            accountService.getAccountsInfo(sendMessage);
        }
        if (callbackQuery.getData().equals("refactor account")) {
            accountService.getAccounts(sendMessage,callbackQuery.getData());
        }
        if (callbackQuery.getData().contains("account update uuid:")) {
          accountService.preUpdateAccount(callbackQuery.getData(),sendMessage);
        }
        //operations
        if (callbackQuery.getData().equals("get operations")) {
            operationService.getOperationsForm(sendMessage);
        }
        if(callbackQuery.getData().equals("refactor operation")){
            operationService.preUpdateOperationForm(sendMessage);
        }
        if (callbackQuery.getData().equals("register")) {
            userService.registerForm(sendMessage);
        }
        if (callbackQuery.getData().equals("login")) {
            userService.loginForm(sendMessage);
        }
        if (callbackQuery.getData().contains("logout")) {
            String[] split = callbackQuery.getData().split(",");
            userService.logout(split[1],sendMessage);
        }
        if (callbackQuery.getData().equals("delete operation")) {
           operationService.deleteOperationForm(sendMessage);
        }
        if (callbackQuery.getData().equals("categories")){
           sendMessage.setText(crossServiceGetter.getCategories(sendMessage)
                   .toString());
        }
        if (callbackQuery.getData().equals("currencies")){
            sendMessage.setText(crossServiceGetter.getCurrencies(sendMessage)
                    .toString());
        }
        //reports
        if (callbackQuery.getData().equals("reports")){
            reportService.reportsResponse(sendMessage);
        }
        //choose report type
        if (callbackQuery.getData().equals("add report")){
            reportService.chooseReport(sendMessage);
        }
        if (callbackQuery.getData().equals("BALANCE")){
            reportService.balanceReportForm(sendMessage);
        }
        if (callbackQuery.getData().equals("BY_DATE")){
            reportService.byDateReportForm(sendMessage);
        }
        if (callbackQuery.getData().equals("BY_CATEGORY")){
            reportService.byCategoryReportForm(sendMessage);
        }
        if (callbackQuery.getData().equals("get reports")){
            reportService.getReports(sendMessage);
        }



        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    @Override
    public String getBotUsername() {
        return "kirikantFinanceBot";
    }

    @Override
    public String getBotToken() {
        return "5396998341:AAHWYoTZ7-wLzYNhgBXTavQMH4a5hNdLs1c";
    }


}
