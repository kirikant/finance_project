//package com.accounts;
//
//import com.accounts.entity.*;
//import com.accounts.repositories.AccountRepo;
//import com.accounts.repositories.BalanceRepo;
//
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import java.time.ZoneOffset;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@AutoConfigureMockMvc
//@RunWith(SpringRunner.class)
//@SpringBootTest
//public class AccountServiceTest {
//
//    @Autowired
//    private AccountRepo accountRepo;
//    @Autowired
//    BalanceRepo balanceRepo;
//
//    @Autowired
//    private MockMvc mockMvc;
//    private  LocalDateTime now=LocalDateTime.now();
//    private BalanceEntity balanceEntity = new BalanceEntity();
//    private AccountEntity accountEntity = new AccountEntity();
//
//
//    public void init(){
//
//        balanceEntity.setBalance(BigDecimal.ZERO);
//        balanceEntity.setDtCreate(now);
//
//        accountEntity.setDescription("test");
//        accountEntity.setDtCreate(now);
//        accountEntity.setTitle("test");
//        accountEntity.setType(Type.CASH);
//        accountEntity.set
//
//        accountEntity.setBalance(balanceEntity);
//        accountEntity=accountRepo.save(accountEntity);
//
//    }
//
////    public void resetDb() {
////        currencyRepo.deleteById(currencyEntity.getUuid());
////        categoryRepo.deleteById(categoryEntity.getUuid());
////        accountRepo.delete();
////    }
//
//
//    @Test
//    public void addAccountTest() throws Exception {
//        init();
//        mockMvc.perform(MockMvcRequestBuilders.post("/account")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\n" +
//                        "  \"title\": \"test\",\n" +
//                        "  \"description\": \"the test payment account\",\n" +
//                        "  \"type\": \"CASH\",\n" +
//                        "  \"currency\":\""+currencyEntity.getUuid()+"\"\n" +
//                        "}"))
//                .andExpect(status().is2xxSuccessful());
//    }
//
//    @Test
//    public void getAccounts() throws Exception{
//        init();
//        mockMvc.perform(MockMvcRequestBuilders.get("/account")
//                        .param("page","0")
//                        .param("size","1"))
//                .andExpect(status().is2xxSuccessful());
//    }
//
//    @Test
//    public void getAccount() throws Exception{
//        init();
//        mockMvc.perform(MockMvcRequestBuilders.get("/account/"+accountEntity.getUuid()))
//                .andExpect(status().is2xxSuccessful());
//    }
//
//    @Test
//    public void updateAccount() throws Exception{
//        init();
//        long epochMilli = accountEntity.getDtUpdate().toInstant(ZoneOffset.UTC).toEpochMilli();
//        mockMvc.perform(MockMvcRequestBuilders.put("/account/"+accountEntity.getUuid()
//                +"/dt_update/"+epochMilli)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content("{\n" +
//                        "  \"title\": \"test\",\n" +
//                        "  \"description\": \"the test payment account\",\n" +
//                        "  \"type\": \"CASH\",\n" +
//                        "  \"currency\":\""+currencyEntity.getUuid()+"\"\n" +
//                        "}"))
//                .andExpect(status().is2xxSuccessful());
//    }
//}
