package com.telegram;

import com.telegram.bot.FinanceBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import javax.annotation.PostConstruct;


@SpringBootApplication
public class TelegramApplication {
	
	private final  FinanceBot financeBot1;

	public TelegramApplication(FinanceBot financeBot1) {
		this.financeBot1 = financeBot1;
	}

	public static void main(String[] args) {

		SpringApplication.run(TelegramApplication.class, args);

	}

	@PostConstruct
	public  void construct(){
		try {
			TelegramBotsApi botsApi =
					new TelegramBotsApi(
							DefaultBotSession.class);
			BotSession botSession = botsApi.registerBot(financeBot1);
		} catch (TelegramApiException e) {
			e.printStackTrace();
		}
	}



}
