package com.example.ex2;

import com.example.ex2.repository.MemoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Ex2ApplicationTests {
	@Autowired
	MemoRepository memoRepository;

	@Test
	void contextLoads(){
		System.out.println(">>" +memoRepository.getClass().getName());
	}
}
