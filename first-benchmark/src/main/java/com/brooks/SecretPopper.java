/*package com.brooks;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.Iterator;
import java.util.Scanner;
import java.util.concurrent.ConcurrentLinkedDeque;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

public class SecretPopper implements Runnable{
	
	@State(Scope.Thread)
	public static class UserState {
		
		@Setup(Level.Trial)
        public void doSetup() {
        	if(!dougLeaMode){
        		feed = new MichaelScottQueue();
        	}else{
        		dlFeed = new ConcurrentLinkedDeque<Node>();
        	}
        	try{
            	fileScanner = new Scanner(f);
        	}catch(FileNotFoundException e){
        		e.printStackTrace();
        	}
            fileSize = Integer.parseInt(fileScanner.nextLine());
            for(int i = 0; i < 10; i++){
            	String s = fileScanner.nextLine();
            	if(!dougLeaMode){
            		feed.put(s, ThreadLocalRandom.current().nextInt(1, DEPRESSION_WRITE_BOUND));
            	}else{
            		Node n = new Node(s, ThreadLocalRandom.current().nextInt(1, DEPRESSION_WRITE_BOUND));
            		dlFeed.add(n);
            	}
            }
            fileScanner.close();
        }

        public boolean dougLeaMode = false;
		public MichaelScottQueue feed;
		public ConcurrentLinkedDeque<Node> dlFeed;
		public Scanner fileScanner;
		public int fileSize;
		public final int DEPRESSION_WRITE_BOUND = 5;
		public File f = new File("/Users/jacobbrooks/Desktop/375as2/jmh/sentences.txt");

	}
	
	public void run() {
		//execute();
	}

	//@Benchmark @BenchmarkMode(Mode.Throughput) @OutputTimeUnit(TimeUnit.MILLISECONDS)
	public void execute(UserState state){
		while(true) {
			if(!state.dougLeaMode){
				if(state.feed.size() >= 50){
					for(int i = 0; i < 20; i++){
						state.feed.poll();
					}
				}
			}else{
				if(state.dlFeed.size() >= 50){
					for(int i = 0; i < 20; i++){
						state.dlFeed.poll();
					}
				}
			}
		}
	}
}
*/
