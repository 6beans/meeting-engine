package ru.sixbeans.meetingengine.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.sixbeans.meetingengine.service.FileFetchingService;

import java.util.Optional;

import static org.springframework.http.HttpMethod.GET;

@Service
@RequiredArgsConstructor
public class RestTemplateFileFetchingService implements FileFetchingService {

    private final RestTemplate restTemplate;

    public Optional<byte[]> get(String fileUrl) {
        var response = restTemplate.exchange(fileUrl, GET, null, byte[].class);
        return Optional.ofNullable(response.getBody());
    }
}
