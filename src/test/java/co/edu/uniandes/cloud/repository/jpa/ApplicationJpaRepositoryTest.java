package co.edu.uniandes.cloud.repository.jpa;

import co.edu.uniandes.cloud.CloiceApp;
import co.edu.uniandes.cloud.domain.Application;
import co.edu.uniandes.cloud.domain.Contest;
import co.edu.uniandes.cloud.domain.User;
import co.edu.uniandes.cloud.domain.enumeration.ApplicationState;
import co.edu.uniandes.cloud.repository.EntitiesBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloiceApp.class)
@Transactional
public class ApplicationJpaRepositoryTest {

    @Autowired
    private ApplicationJpaRepository appRepo;

    @Autowired
    private ContestJpaRepository contestRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final EntitiesBuilder builder = new EntitiesBuilder();
    private User user;

    @Before
    public void setUp() {
        assertNotNull(appRepo);
        assertNotNull(contestRepo);
        assertNotNull(userRepo);
        user = builder.buildUser();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepo.save(user);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void findByContest() {
        final Contest contest = builder.buildContest();
        contest.user(user);
        contestRepo.save(contest);

        assertThat(appRepo.findAll().size(), is(0));

        initApplications(3, contest);

        final List<Application> all = appRepo.findAll();
        assertEquals(3, all.size());

        Page<Application> byContest = appRepo.findByContest(new PageRequest(0, 1), new Contest(contest.getId()));
        assertThat(byContest.getTotalPages(), is(3));
    }

    @Test
    public void findByStatusAndContest() {
        final Contest contest = builder.buildContest();
        contest.user(user);
        contestRepo.save(contest);

        final Contest contest2 = builder.buildContest();
        contest2.user(user);
        contestRepo.save(contest2);

        initApplications(4, contest);
        initApplications(2, contest2);

        final Page<Application> byStatusConverted = appRepo.findByStatusAndContest(new PageRequest(0, 1), ApplicationState.CONVERTED, new Contest(contest.getId()));
        assertThat(byStatusConverted.getTotalPages(), is(2));
        byStatusConverted.getContent()
            .forEach(app -> assertThat(app.getStatus(), is(ApplicationState.CONVERTED)));
        byStatusConverted.getContent()
            .forEach(app -> assertThat(app.getContest().getId(), is(contest.getId())));

        final Page<Application> byStatusInProcess = appRepo.findByStatusAndContest(new PageRequest(0, 1), ApplicationState.IN_PROCESS, new Contest(contest.getId()));
        assertThat(byStatusInProcess.getTotalPages(), is(2));
        byStatusInProcess.getContent()
            .forEach(app -> assertThat(app.getStatus(), is(ApplicationState.IN_PROCESS)));
        byStatusConverted.getContent()
            .forEach(app -> assertThat(app.getContest().getId(), is(contest.getId())));
    }

    @Test
    public void findByStatus() {
        final Contest contest = builder.buildContest();
        contest.user(user);
        contestRepo.save(contest);

        initApplications(7, contest);

        final List<Application> byStatusConverted = appRepo.findByStatus(ApplicationState.CONVERTED);
        assertThat(byStatusConverted.size(), is(4));

        final List<Application> byStatusInProcess = appRepo.findByStatus(ApplicationState.IN_PROCESS);
        assertThat(byStatusInProcess.size(), is(3));
    }

    private void initApplications(int howMany, Contest contest) {
        for (int i = 0; i < howMany; i++) {
            final Application application = builder.buildApplication();
            application.contest(contest);
            application.status(i % 2 == 0 ? ApplicationState.CONVERTED : ApplicationState.IN_PROCESS);
            appRepo.save(application);
        }
    }
}
