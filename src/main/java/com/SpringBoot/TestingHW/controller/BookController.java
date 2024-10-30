package com.SpringBoot.TestingHW.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/book")
@RequiredArgsConstructor
public class BookController {

}
