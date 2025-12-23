package com.cabservice.cab_service;

import com.cabservice.cab_service.DomainEventPublisher;
import com.cabservice.cab_service.entity.Cab;
import com.cabservice.cab_service.entity.City;
import com.cabservice.cab_service.enums.CabState;
import com.cabservice.cab_service.repository.CabRepository;
import com.cabservice.cab_service.repository.CityRepository;
import com.cabservice.cab_service.service.impl.CabServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CabServiceImplTest {

    @Mock
    private CabRepository cabRepository;

    @Mock
    private CityRepository cityRepository;

    @Mock
    private DomainEventPublisher eventPublisher;

    @InjectMocks
    private CabServiceImpl cabService;

    private City city;

    @BeforeEach
    void setUp() {
        city = new City(10L, "CityA");
    }

    @Test
    void registerCab_createsIdleCabInCityAndRecordsAnalytics() {
        when(cityRepository.findByCityId(10L)).thenReturn(Optional.of(city));

        ArgumentCaptor<Cab> cabCaptor = ArgumentCaptor.forClass(Cab.class);

        Cab cab = cabService.registerCab(10L);

        verify(cabRepository).save(cabCaptor.capture());
        Cab saved = cabCaptor.getValue();

        assertThat(saved.getCityId()).isEqualTo(city.getCityId());
        assertThat(saved.getState()).isEqualTo(CabState.IDLE);
        assertThat(saved.getLastStateChangeTime()).isNotNull();

        // The returned cab should match the saved one in key aspects
        assertThat(cab.getCityId()).isEqualTo(city.getCityId());
        assertThat(cab.getState()).isEqualTo(CabState.IDLE);

        verify(eventPublisher).publish(any());
    }

    @Test
    void registerCab_throwsIfCityNotFound() {
        when(cityRepository.findByCityId(99L)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> cabService.registerCab(99L));

        verifyNoInteractions(cabRepository);
    }

    @Test
    void updateState_changesStateAndRecordsAnalytics() {
        Cab cab = new Cab();
        cab.setCabId(1L);
        cab.setState(CabState.IDLE);
        cab.setCityId(city.getCityId());

        when(cabRepository.findByCabId(1L)).thenReturn(Optional.of(cab));

        cabService.updateState(1L, CabState.ON_TRIP);

        verify(cabRepository).save(cab);
        assertThat(cab.getState()).isEqualTo(CabState.ON_TRIP);
        assertThat(cab.getLastStateChangeTime()).isNotNull();
        assertThat(cab.getCityId()).isNull(); // should be cleared on trip

        verify(eventPublisher).publish(any());
    }

    @Test
    void updateState_noOpWhenStateUnchanged() {
        Cab cab = new Cab();
        cab.setCabId(1L);
        cab.setState(CabState.IDLE);

        when(cabRepository.findByCabId(1L)).thenReturn(Optional.of(cab));

        cabService.updateState(1L, CabState.IDLE);

        verify(cabRepository, never()).save(any());
        verifyNoInteractions(eventPublisher);
    }

    @Test
    void updateLocation_updatesCityWhenCabIdle() {
        Cab cab = new Cab();
        cab.setCabId(1L);
        cab.setState(CabState.IDLE);
        cab.setCityId(city.getCityId());

        City newCity = new City(20L, "CityB");

        when(cabRepository.findByCabId(1L)).thenReturn(Optional.of(cab));
        when(cityRepository.findByCityId(20L)).thenReturn(Optional.of(newCity));

        cabService.updateLocation(1L, 20L);

        verify(cabRepository).save(cab);
        assertThat(cab.getCityId()).isEqualTo(newCity.getCityId());
    }

    @Test
    void updateLocation_throwsWhenCabOnTrip() {
        Cab cab = new Cab();
        cab.setCabId(1L);
        cab.setState(CabState.ON_TRIP);

        when(cabRepository.findByCabId(1L)).thenReturn(Optional.of(cab));

        assertThrows(IllegalStateException.class, () -> cabService.updateLocation(1L, 20L));
    }
}
