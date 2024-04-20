package ru.sixbeans.meetingengine.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

import static org.springframework.http.HttpMethod.GET;

@Service
@RequiredArgsConstructor
public class FileFetchingService {

    private final RestTemplate restTemplate;

    public Optional<byte[]> get(String photoUrl) {
        var response = restTemplate.exchange(photoUrl, GET, null, byte[].class);
        return Optional.ofNullable(response.getBody());
    }
}
