package com.maco.followthebeat.v2.faker;

import com.github.javafaker.Faker;
import com.maco.followthebeat.v2.core.entity.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
public class FakeEntityFactory {
    private final Faker faker =  new Faker();

    public Artist fakeArtist(){
        return Artist.builder()
                .name(faker.rockBand().name())
                .imgUrl(faker.internet().avatar())
                .genres(List.of(faker.music().genre(), faker.music().genre()))
                .build();
    }

    public Stage fakeStage(Festival festival) {
        Stage stage = new Stage();
        stage.setName(faker.rockBand().name() + " Stage");
        stage.setImgUrl(faker.internet().image());
        stage.setFestival(festival);
        return stage;
    }

    public Venue fakeVenue() {
        Venue venue = new Venue();
        venue.setName(faker.company().name());
        venue.setImgUrl(faker.internet().image());
        venue.setAddress(faker.address().streetAddress());
        venue.setCity(faker.address().city());
        venue.setCountry(faker.address().country());
        venue.setCapacity(faker.number().numberBetween(500, 50000));
        return venue;
    }

    public Festival fakeFestival() {
        LocalDate start = faker.date().future(30, java.util.concurrent.TimeUnit.DAYS).toInstant()
                .atZone(java.time.ZoneId.systemDefault()).toLocalDate();
        LocalDate end = start.plusDays(faker.number().numberBetween(1, 5));

        return Festival.builder()
                .name(faker.music().genre() + " Festival")
                .description(faker.lorem().sentence())
                .location(faker.address().cityName())
                .startDate(start)
                .endDate(end)
                .logoUrl(faker.internet().url())
                .websiteUrl(faker.internet().url())
                .isActive(true)
                .artists(new ArrayList<>())
                .stages(new HashSet<>())
                .build();
    }

    public Schedule fakeSchedule() {
        return Schedule.builder()
                .date(LocalDate.now().plusDays(faker.number().numberBetween(1, 30)))
                .startTime(LocalTime.of(faker.number().numberBetween(16, 23), 0))
                .build();
    }

    public Concert fakeConcert(Artist artist, Location location) {
        Schedule schedule = fakeSchedule();
        Concert concert = Concert.builder()
                .artist(artist)
                .location(location)
                .schedule(schedule)
                .build();
        schedule.setConcert(concert);
        return concert;
    }
}
