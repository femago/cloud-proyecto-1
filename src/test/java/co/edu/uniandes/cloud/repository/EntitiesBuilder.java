package co.edu.uniandes.cloud.repository;

import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.domain.User;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;

public class EntitiesBuilder {

    public static final String ID_CONTEST = "789456";
    public static final String EMAIL = "f@g.com";

    public Application buildApplication() {
        final Application entity = new Application();
        final Contest contest = buildContest();
        entity.contest(contest)
            .name("nombre")
            .lastname("apellido")
            .email(EMAIL)
            .originalRecord(new byte[]{1})
            .createDate(Instant.now())
            .originalRecordContentType("audio/mp3")
            .notes("nota")
            .originalRecordLocation("origLocation")
            .convertedRecordLocation("converLocation")
            .status(ApplicationState.IN_PROCESS);
        return entity;
    }

    public Contest buildContest() {
        final Contest contest = new Contest();
        contest
            .name("Nombre concurso")
            .uuid("uuid")
            .createDate(Instant.now())
            .startDate(LocalDate.now())
            .endDate(LocalDate.now())
            .price(BigDecimal.ONE)
            .script("Script")
            .user(buildUser());

        return contest;
    }

    public User buildUser() {
        final User user = new User();
        user.setLogin("login");
        user.setPassword("pass");
        user.setActivated(true);
        return user;
    }

}
