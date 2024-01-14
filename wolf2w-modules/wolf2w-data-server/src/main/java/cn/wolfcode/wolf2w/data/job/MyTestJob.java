package cn.wolfcode.wolf2w.data.job;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class MyTestJob {
//    @Scheduled(fixedDelay = 2000)
    public void printTime(){
        System.out.println(LocalDateTime.now());
    }
}
