package com.codestates.practice.assignment;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Week1 {
    //1. ["Blenders", "Old", "Johnnie"] 와 "[Pride", "Monk", "Walker"] 를 순서대로 하나의 스트림으로 처리되는 로직 검증
    @Test
    public void assignment1() {
        Flux<String> names1 = Flux.just("Blenders", "Old", "Johnnie")
                .delayElements(Duration.ofSeconds(1));
        Flux<String> names2 = Flux.just("Pride", "Monk", "Walker")
                .delayElements(Duration.ofSeconds(1));
        Flux<String> names = Flux.concat(names1, names2)
                .log();

        StepVerifier.create(names)
                .expectSubscription()
                .expectNext("Blenders", "Old", "Johnnie", "Pride", "Monk", "Walker")
                .verifyComplete();
    }

    // 2.1~100 까지의 자연수 중 짝수만 출력하는 로직 검증
    @Test
    public void assignment2() {
        Flux<Integer> flux = Flux.range(1, 100).filter(n -> n % 2 == 0).log();

        // doOnNext() : Flux가 Subscriber에 next 신호를 발생할 때 불림.
        flux.doOnNext(n -> System.out.println("doOnNext : " + n))
                .subscribe(n -> System.out.println("received : " + n));

        StepVerifier.create(flux)
                .expectSubscription()
                .thenConsumeWhile(i -> i%2 == 0)
                //.expectNextCount(flux.count().block())
                .verifyComplete();

        // second one line
        // Flux.range(1, 100).filter(num -> num % 2 == 0).subscribe(x -> System.out.println(x));

    }

    // 3. “hello”, “there” 를 순차적으로 publish하여 순서대로 나오는지 검증
    @Test
    public void assignment3() {

        Flux<String> flux = Flux.just("hello", "there").log();

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext("hello", "there")
                .verifyComplete();
    }

    // 4. 아래와 같은 객체가 전달될 때 “JOHN”, “JACK” 등 이름이 대문자로 변환되어 출력되는 로직 검증
    //    Person("John", "[john@gmail.com](mailto:john@gmail.com)", "12345678")
    //    Person("Jack", "[jack@gmail.com](mailto:jack@gmail.com)", "12345678")
    @Test
    public void assignment4() {

        Flux<Person> flux = Flux.just(new Person("John","[john@gmail.com](mailto:john@gmail.com)","12345678"),new Person("Jack","[jack@gmail.com](mailto:jack@gmail.com)","12345678"))
                .map(p -> {
                    return p.nameToUpperCase();
                });

        flux.doOnNext(p -> System.out.println("doOnNext : " + p.getName()))
                .subscribe(p -> System.out.println("received : " + p.getName()));

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNextMatches(p -> p.getName().equals("JOHN"))
                .expectNextMatches(p -> p.getName().equals("JACK"))
                .verifyComplete();
    }

    @Getter @Setter
    class Person {
        String name;
        String email;
        String phone;

        public Person(String name, String email, String phone){
            this.name = name;
            this.email = email;
            this.phone = phone;
        }

        public Person nameToUpperCase(){
            this.name=this.getName().toUpperCase();
            return this;
        }

        public String getName(){
            return name;
        }
    }

    // 5. ["Blenders", "Old", "Johnnie"] 와 ["Pride", "Monk", "Walker"]를 압축하여 스트림으로 처리 검증
    //    예상되는 스트림 결과값 ["Blenders Pride", "Old Monk", "Johnnie Walker”]
    @Test
    public void assignment5() {

        Flux<String> flux = Flux.just("Blenders", "Old", "Johnnie");
        Flux<String> flux2 = Flux.just("Pride", "Monk", "Walker");

        Flux<String> zipFlux = flux.zipWith(flux2, (f, f2) -> f + " " +f2).log();

        StepVerifier.create(zipFlux)
                .expectSubscription()
                .expectNext("Blenders Pride", "Old Monk", "Johnnie Walker")
                .verifyComplete();
    }

    // 6. ["google", "abc", "fb", "stackoverflow”] 의 문자열 중 5자 이상 되는 문자열만 대문자로 비동기로 치환하여 1번 반복하는 스트림으로 처리하는 로직 검증
    //     예상되는 스트림 결과값 ["GOOGLE", "STACKOVERFLOW", "GOOGLE", "STACKOVERFLOW"]
    @Test
    public void assignment6() {

        Flux<String> flux = Flux.just("google", "abc", "fb", "stackoverflow")
                .filter(s -> s.length() >= 5)
                .flatMap(s -> Flux.just(s.toUpperCase()))
                .subscribeOn(Schedulers.parallel())
                .repeat(1)
                .log();

        StepVerifier.create(flux)
                .expectSubscription()
                .expectNext("GOOGLE", "STACKOVERFLOW", "GOOGLE", "STACKOVERFLOW")
                .verifyComplete();
    }
}