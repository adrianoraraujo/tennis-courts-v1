package com.tenniscourts.reservations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tenniscourts.guests.GuestController;
import com.tenniscourts.guests.GuestService;
import com.tenniscourts.reservations.ReservationController;
import com.tenniscourts.reservations.ReservationService;
import com.tenniscourts.reservations.model.ReservationDTO;
import com.tenniscourts.reservations.stubs.TennisCourtStubs;
import com.tenniscourts.schedules.ScheduleController;
import com.tenniscourts.schedules.ScheduleService;
import com.tenniscourts.schedules.model.ScheduleDTO;
import com.tenniscourts.tenniscourts.TennisCourtController;
import com.tenniscourts.tenniscourts.TennisCourtService;
import com.tenniscourts.tenniscourts.model.TennisCourtDTO;
import org.hamcrest.Matchers;
import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.tenniscourts.reservations.stubs.GuestStubs.createGuestDTOStub;
import static com.tenniscourts.reservations.stubs.GuestStubs.guestDTOStub;
import static com.tenniscourts.reservations.stubs.ReservationStubs.createReservationRequestDTOStub;
import static com.tenniscourts.reservations.stubs.ReservationStubs.reservationDTOStub;
import static com.tenniscourts.reservations.stubs.ScheduleStubs.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest
@ContextConfiguration(classes = {
        ReservationController.class,
        GuestController.class,
        ScheduleController.class,
        TennisCourtController.class,
        ReservationService.class,
        GuestService.class,
        ScheduleService.class,
        TennisCourtService.class})
class ControllerTests {


    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReservationService reservationService;

    @MockBean
    GuestService guestService;

    @MockBean
    ScheduleService scheduleService;

    @MockBean
    TennisCourtService tennisCourtService;


    //ReservationController tests
    @Test
    void bookReservationShouldReturnStatusCREATED() throws Exception {
        var createReservationDtoStub = createReservationRequestDTOStub();
        var reservationDtoStub = reservationDTOStub();

        var json = new ObjectMapper().writeValueAsString(createReservationDtoStub);

        when(reservationService.bookReservation(createReservationDtoStub)).thenReturn(reservationDtoStub);

        this.mockMvc.perform(post("/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void findReservationByIdShouldReturnStatusOKAndTheReservation() throws Exception {
        var reservatioDtoStub = reservationDTOStub();

        when(reservationService.findReservationById(1L)).thenReturn(reservatioDtoStub);

        this.mockMvc.perform(get("/reservation/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("reservationStatus", Matchers.is("READY_TO_PLAY")))
                .andExpect(jsonPath("schedule.id", Matchers.is(1)))
                .andExpect(jsonPath("schedule.startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("schedule.endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("schedule.tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("schedule.tennisCourt.name", Matchers.is("Test Court")))
                .andExpect(jsonPath("scheduledId", Matchers.is(1)))
                .andExpect(jsonPath("value", Matchers.is(10)))
                .andExpect(jsonPath("guestId", Matchers.is(1)));
    }

    @Test
    void cancelReservationShouldReturnStatusOKAndTheReservation() throws Exception {
        var reservatioDtoStub = reservationDTOStub();
        reservatioDtoStub.setReservationStatus("CANCELLED");
        reservatioDtoStub.setRefundValue(new BigDecimal(5));
        reservatioDtoStub.setValue(new BigDecimal(5));

        when(reservationService.cancelReservation(1L)).thenReturn(reservatioDtoStub);

        this.mockMvc.perform(get("/reservation/cancel/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("reservationStatus", Matchers.is("CANCELLED")))
                .andExpect(jsonPath("schedule.id", Matchers.is(1)))
                .andExpect(jsonPath("schedule.startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("schedule.endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("schedule.tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("schedule.tennisCourt.name", Matchers.is("Test Court")))
                .andExpect(jsonPath("scheduledId", Matchers.is(1)))
                .andExpect(jsonPath("refundValue", Matchers.is(5)))
                .andExpect(jsonPath("value", Matchers.is(5)))
                .andExpect(jsonPath("guestId", Matchers.is(1)));
    }

    @Test
    void rescheduleReservationShouldReturnStatusOKAndTheReservation() throws Exception {
        var reservatioDtoStub = reservationDTOStub();
        reservatioDtoStub.setId(2L);
        reservatioDtoStub.setScheduledId(2L);
        reservatioDtoStub.getSchedule().setId(2L);
        reservatioDtoStub.setPreviousReservation(
                ReservationDTO.builder()
                        .id(1L)
                        .schedule(ScheduleDTO.builder()
                                .id(1L)
                                .tennisCourt(TennisCourtDTO.builder()
                                        .id(1L)
                                        .name("Test Court")
                                        .build())
                                .startDateTime(LocalDateTime.of(2021, 8, 8, 8, 0))
                                .endDateTime(LocalDateTime.of(2021, 8, 8, 9, 0))
                                .build())
                        .reservationStatus("RESCHEDULED")
                        .refundValue(new BigDecimal(10))
                        .value(new BigDecimal(0))
                        .scheduledId(1L)
                        .guestId(1L)
                        .build());

        when(reservationService.rescheduleReservation(1L, 2L)).thenReturn(reservatioDtoStub);

        this.mockMvc.perform(get("/reservation/reschedule")
                        .param("reservationId", "1")
                        .param("scheduleId", "2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(2)))
                .andExpect(jsonPath("reservationStatus", Matchers.is("READY_TO_PLAY")))

                .andExpect(jsonPath("previousReservation.id", Matchers.is(1)))
                .andExpect(jsonPath("previousReservation.schedule.id", Matchers.is(1)))
                .andExpect(jsonPath("previousReservation.schedule.tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("previousReservation.schedule.tennisCourt.name", Matchers.is("Test Court")))
                .andExpect(jsonPath("previousReservation.schedule.startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("previousReservation.schedule.endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("previousReservation.reservationStatus", Matchers.is("RESCHEDULED")))
                .andExpect(jsonPath("previousReservation.refundValue", Matchers.is(10)))
                .andExpect(jsonPath("previousReservation.value", Matchers.is(0)))
                .andExpect(jsonPath("previousReservation.scheduledId", Matchers.is(1)))
                .andExpect(jsonPath("previousReservation.guestId", Matchers.is(1)))

                .andExpect(jsonPath("schedule.id", Matchers.is(2)))
                .andExpect(jsonPath("schedule.startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("schedule.endDateTime", Matchers.is("2021-08-08T09:00")))

                .andExpect(jsonPath("schedule.tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("schedule.tennisCourt.name", Matchers.is("Test Court")))

                .andExpect(jsonPath("scheduledId", Matchers.is(2)))
                .andExpect(jsonPath("value", Matchers.is(10)))
                .andExpect(jsonPath("guestId", Matchers.is(1)));
    }

    @Test
    void reservationHistoricShouldReturnStatusOKAndAListOfReservations() throws Exception {
        var reservatioDtoStub = reservationDTOStub();

        when(reservationService.findPastReservations()).thenReturn(List.of(reservatioDtoStub));

        this.mockMvc.perform(get("/reservation/history"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].reservationStatus", Matchers.is("READY_TO_PLAY")))
                .andExpect(jsonPath("$[0].schedule.id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].schedule.startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("$[0].schedule.endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("$[0].schedule.tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].schedule.tennisCourt.name", Matchers.is("Test Court")))
                .andExpect(jsonPath("$[0].scheduledId", Matchers.is(1)))
                .andExpect(jsonPath("$[0].value", Matchers.is(10)))
                .andExpect(jsonPath("$[0].guestId", Matchers.is(1)));
    }

    //GuestController tests
    @Test
    @Disabled
    void saveGuestShouldReturnStatusCREATED() throws Exception {
        var createGuestDTOStub = createGuestDTOStub();
        var guestDTOStub = guestDTOStub();

        var json = new ObjectMapper().writeValueAsString(createGuestDTOStub);

        when(guestService.saveGuest(createGuestDTOStub)).thenReturn(guestDTOStub);

        this.mockMvc.perform(post("/guest")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void findGuestByIdtShouldReturnStatusOKAndTheGuest() throws Exception {
        var guestDTOStub = guestDTOStub();

        when(guestService.findGuestById(1L)).thenReturn(guestDTOStub);

        this.mockMvc.perform(get("/guest/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("name", Matchers.is("Andrews")));
    }

    @Test
    void findByGuestNameShouldReturnStatusOKAndAListOfGuests() throws Exception {
        var guestDTOStub = guestDTOStub();

        when(guestService.findByGuestName("and")).thenReturn(List.of(guestDTOStub));

        this.mockMvc.perform(get("/guest/find/and"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Andrews")));
    }

    @Test
    void findAllGuestsShouldReturnStatusOKAndAListOfGuests() throws Exception {
        var guestDTOStub = guestDTOStub();

        when(guestService.findAllGuests()).thenReturn(List.of(guestDTOStub));

        this.mockMvc.perform(get("/guest"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].name", Matchers.is("Andrews")));
    }

    @Test
    @Disabled
    void updateGuestShouldReturnStatusOKAndTheGuest() throws Exception {
        var createGuestDTOStub = createGuestDTOStub();
        createGuestDTOStub.setName("Andrews Souza");
        var guestDTOStub = guestDTOStub();
        guestDTOStub.setName("Andrews Souza");

        var json = new ObjectMapper().writeValueAsString(createGuestDTOStub);

        when(guestService.updateGuest(1L, createGuestDTOStub)).thenReturn(guestDTOStub);

        this.mockMvc.perform(put("/guest/1")
                        .content(json)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("name", Matchers.is("Andrews Souza")));
    }

    @Test
    void deleteGuestByIdtShouldReturnStatusNOCONTENT() throws Exception {
        this.mockMvc.perform(delete("/guest/1"))
                .andExpect(status().isNoContent());
    }

    //ScheduleController tests
    @Test
    @Disabled
    void addScheduleShouldReturnStatusCREATED() throws Exception {


        var createScheduleDtoStub = createScheduleRequestDTOStub();

        var scheduleDtoStub = scheduleDTOStub();

        var jsonObject = new JSONObject();
        jsonObject.put("tennisCourtId", 1L);
        jsonObject.put("startDateTime", "2021-08-08T08:00");

        var json = jsonObject.toString();

        when(scheduleService.addSchedule(createScheduleDtoStub)).thenReturn(scheduleDtoStub);

        this.mockMvc.perform(post("/schedule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void findScheduleByIdShouldReturnASchedule() throws Exception {
        var scheduleDtoStub = scheduleDTOStub();

        when(scheduleService.findScheduleById(1L)).thenReturn(scheduleDtoStub);

        this.mockMvc.perform(get("/schedule/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("id", Matchers.is(1)))
                .andExpect(jsonPath("startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("tennisCourt.name", Matchers.is("Test Court")));
    }

    @Test
    void findFreeSchedulesByDatesShouldReturnAListOfSchedules() throws Exception {
        var scheduleDtoStub = scheduleDTOStub();

        LocalDateTime startDateTime = LocalDateTime.of(2021, 8, 8, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 8, 10, 23, 59);

        when(scheduleService.findFreeSchedulesByDates(startDateTime, endDateTime)).thenReturn(List.of(scheduleDtoStub));

        this.mockMvc.perform(get("/schedule/freeslots")
                        .param("startDate", "2021-08-08")
                        .param("endDate", "2021-08-10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("$[0].endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("$[0].tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].tennisCourt.name", Matchers.is("Test Court")));
    }

    @Test
    void findFreeSchedulesByTennisCourtAndDatesShouldReturnAListOfSchedules() throws Exception {
        var scheduleDtoStub = scheduleDTOStub();

        LocalDateTime startDateTime = LocalDateTime.of(2021, 8, 8, 0, 0);
        LocalDateTime endDateTime = LocalDateTime.of(2021, 8, 10, 23, 59);

        when(scheduleService.findFreeSchedulesByDates(startDateTime, endDateTime)).thenReturn(List.of(scheduleDtoStub));

        this.mockMvc.perform(get("/schedule/freeslots")
                        .param("startDate", "2021-08-08")
                        .param("endDate", "2021-08-10")
                        .param("TennisCourt", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", Matchers.is(1)))
                .andExpect(jsonPath("$[0].id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].startDateTime", Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("$[0].endDateTime", Matchers.is("2021-08-08T09:00")))
                .andExpect(jsonPath("$[0].tennisCourt.id", Matchers.is(1)))
                .andExpect(jsonPath("$[0].tennisCourt.name", Matchers.is("Test Court")));
    }

    //TennisCourtController tests
    @Test
    void addTennisCourtShouldReturnStatusCREATED() throws Exception {
        var tennisCourtDTOStub = TennisCourtStubs.tennisCourtDTOStub();

        when(tennisCourtService.addTennisCourt(tennisCourtDTOStub)).thenReturn(tennisCourtDTOStub);

        var json = new ObjectMapper().writeValueAsString(tennisCourtDTOStub);

        this.mockMvc.perform(post("/tennis-court")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }

    @Test
    void findTennisCourtByIdShouldReturnStatusOKAndATennisCourt() throws Exception {
        var tennisCourtDTOStub = TennisCourtStubs.tennisCourtDTOStub();

        when(tennisCourtService.findTennisCourtById(1L)).thenReturn(tennisCourtDTOStub);

        this.mockMvc.perform(get("/tennis-court/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id",Matchers.is(1)))
                .andExpect(jsonPath("name",Matchers.is("Test Court")));
    }

    @Test
    void findTennisCourtWitchSchedulesByIdShouldReturnStatusOKAndATennisCourt() throws Exception {
        var tennisCourtDTOStub = TennisCourtStubs.tennisCourtDTOStub();
        tennisCourtDTOStub.setTennisCourtSchedules(List.of(scheduleDTOStub()));

        when(tennisCourtService.findTennisCourtById(1L)).thenReturn(tennisCourtDTOStub);

        this.mockMvc.perform(get("/tennis-court/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id",Matchers.is(1)))
                .andExpect(jsonPath("name",Matchers.is("Test Court")))
                .andExpect(jsonPath("tennisCourtSchedules[0].id",Matchers.is(1)))
                .andExpect(jsonPath("tennisCourtSchedules[0].tennisCourt.id",Matchers.is(1)))
                .andExpect(jsonPath("tennisCourtSchedules[0].tennisCourt.name",Matchers.is("Test Court")))
                .andExpect(jsonPath("tennisCourtSchedules[0].startDateTime",Matchers.is("2021-08-08T08:00")))
                .andExpect(jsonPath("tennisCourtSchedules[0].endDateTime",Matchers.is("2021-08-08T09:00")));
    }


}
