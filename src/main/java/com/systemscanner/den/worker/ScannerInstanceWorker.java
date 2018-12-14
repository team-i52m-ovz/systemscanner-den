package com.systemscanner.den.worker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jna.Native;
import com.systemscanner.den.model.ReportDto;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import oshi.SystemInfo;
import oshi.hardware.Display;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.platform.windows.WindowsBaseboard;
import oshi.hardware.platform.windows.WindowsDisplay;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScannerInstanceWorker {
	private final ObjectMapper objectMapper;
	private final RestTemplate restTemplate;

	@Value("${systemscanner.api.url}")
	private String url;

	@Value("${systemscanner.instance.pid}")
	private String pid;

	@Value("${systemscanner.instance.key}")
	private String key;

	@Scheduled(fixedRate = 5000L)
	public void scan() {
		log.info("Sending scanner info");
		try {
			NativeAccessor accessor = Native.loadLibrary(NativeAccessor.class);

			val headers = new HashMap<String, String>();
			headers.put("scanner-pid", pid);
			headers.put("scanner-token", key);

			this.restTemplate.postForEntity(String.format("%s/rat", url), accessor.generateReport(), Void.class, headers);
		} catch (Throwable ex) {
			log.error(ex.getMessage());
		}
	}

}
