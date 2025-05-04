package com.maco.followthebeat.v2.faker;

import com.github.javafaker.Faker;
import com.maco.followthebeat.v2.core.entity.Artist;
import com.maco.followthebeat.v2.core.entity.Concert;
import com.maco.followthebeat.v2.core.entity.Festival;
import com.maco.followthebeat.v2.core.entity.Stage;
import com.maco.followthebeat.v2.core.service.interfaces.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FakeAdapter {
    private final Faker faker = new Faker();
    private final FakeEntityFactory fakeEntityFactory;
    private final FestivalService festivalService;
    private final ArtistService artistService;
    private final StageService stageService;
    private final ConcertService concertService;
    private final ScheduleService scheduleService;

    private void generateOneFestival() {
        Festival festival = festivalService.save(fakeEntityFactory.fakeFestival());

        List<Artist> artists = new ArrayList<>();
        for (int i = 0; i < faker.random().nextInt(3, 6); i++) {
            artists.add(artistService.save(fakeEntityFactory.fakeArtist()));
        }

        List<Stage> stages = new ArrayList<>();
        for (int i = 0; i < faker.random().nextInt(1, 3); i++) {
            stages.add(stageService.save(fakeEntityFactory.fakeStage(festival)));
        }

        for (Artist artist : artists) {
            Stage stage = stages.get(faker.random().nextInt(stages.size()));
            Concert concert = fakeEntityFactory.fakeConcert(artist, stage);
            scheduleService.save(concert.getSchedule());
            concertService.save(concert);
        }
    }

    @Transactional
    public void generateAndSaveFakeFestivals(int count) {
        int batchSize = 50;

        for (int i = 0; i < count; i++) {
            generateOneFestival();

            if ((i + 1) % batchSize == 0) {
                log.info("Committing batch of {} festivals (total so far: {})", batchSize, i + 1);
            }
        }
    }

}
