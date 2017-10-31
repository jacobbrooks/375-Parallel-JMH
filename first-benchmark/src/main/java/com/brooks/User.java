package com.brooks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

public class User implements Runnable{

	@State(Scope.Thread)
	public static class UserState {

		@Setup(Level.Trial)
        public void doSetup() {
        	if(!dougLeaMode){
        		feed = new MichaelScottQueue();
        	}else{
        		dlFeed = new ConcurrentLinkedDeque<Node>();
        	}
            for(int i = 0; i < 10; i++){
            	String s = sentences[i];
            	if(!dougLeaMode){
            		feed.put(s, ThreadLocalRandom.current().nextInt(1, DEPRESSION_WRITE_BOUND));
            	}else{
            		Node n = new Node(s, ThreadLocalRandom.current().nextInt(1, DEPRESSION_WRITE_BOUND));
            		dlFeed.add(n);
            	}
            }
        }

		public final int WRITE_PROBABILITY = 50;
		public final int DEPRESSION_WRITE_BOUND = 5;
		public int depression = 0;
		public boolean dougLeaMode = false;
		public MichaelScottQueue feed;
		public ConcurrentLinkedDeque<Node> dlFeed;
		public String[] sentences = {
			"I hate my life",
			"I'm having the worst day ever",
			"I have no friends",
			"My dreams usually involve me sucking in general",
			"Everyone but me is stupid",
			"Me doing poorly in school is due to my teacher's thick Russian accent",
			"My dad disowned me",
			"I can't sleep",
			"I'm incapable of self reflection, except for right now but that's it",
			"corporations have a stranglehold on our government",
			"college is a giant sham to make you buy textbooks",
			"The government is mind-controlling me except for right now",
			"Colorless green ideas sleep furiously, especially for me because I don't have any",
			"If I lost money like I lost friends, I'd be filing for chapter 13 bankruptcy",
			"I am completely worthless",
			"I am dumb",
			"People should really learn how to speak correctibly",
			"A rabbi, a priest and a nun walk into a bar, they see me hitting on an 80 year old bartender",
			"If self-doubt was a liquid, I'd be constantly drowning",
			"How many lightbulbs would it take to screw me, not many because everyone screws me",
			"I'm the kind of person that always has gross earwax",
			"My doctor diagnosed me with chronic existence",
			"The entire universe is expanding accept for my social circle",
			"When the garbage truck comes around, I always hitch a ride in the back"
		};

	}
	
	public void run() {
		//execute();
	}

	@Benchmark @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void execute(UserState state){
		if(state.depression >= 10) {
			writePotentially(state);
		}else{
			readFeed(state);
		}
	}
	
	@Benchmark @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void writePotentially(UserState state) {
		int writeGate = ThreadLocalRandom.current().nextInt(0, state.WRITE_PROBABILITY);
		if(writeGate == 0) {
			pushToFeed(state);
			state.depression = 0;
		}
	}
	
	@Benchmark @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void readFeed(UserState state) {
		Node[] myFeed = getFeed(state);
		if(myFeed == null) {
			return;
		}
		for(int i = 0; i < myFeed.length; i++) {
			int depressionMeasure = myFeed[i].getDepressionMeasure();
			state.depression += depressionMeasure;
		}
	}
	
	public Node[] getFeed(UserState state) {
		if(!state.dougLeaMode) {
			Node[] updates = state.feed.subQueueToNodeArray(5);
			return updates;
		}
		Iterator<Node> dequeueIterator = state.dlFeed.descendingIterator();
		Node[] updates = new Node[5];
		for(int i = 0; i < 5; i++) {
			Node n = dequeueIterator.next();
			updates[i] = n;
		}
		return updates;
	}

	public void pushToFeed(UserState state) {
		if(!state.dougLeaMode) {
			String s = getUpdateFromArray(state);
			state.feed.put(s, ThreadLocalRandom.current().nextInt(1, state.DEPRESSION_WRITE_BOUND));
		}else {
			String s = getUpdateFromArray(state);
			Node n = new Node(s, ThreadLocalRandom.current().nextInt(1, state.DEPRESSION_WRITE_BOUND));
			state.dlFeed.add(n);
		}
	}
	
	
	public String getUpdateFromArray(UserState state) {
		int grabFromLine = ThreadLocalRandom.current().nextInt(0, state.sentences.length);
		String line = state.sentences[grabFromLine];
		return line;
	}
	
}
