package com.nanomt88.risk.kafka.storm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;


import com.google.common.collect.ImmutableList;
import org.apache.storm.topology.BasicOutputCollector;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseBasicBolt;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Tuple;

public class SentenceBolt extends BaseBasicBolt {

	// list used for aggregating the words
	private List<String> words = new ArrayList<String>();

	public void execute(Tuple input, BasicOutputCollector collector) {
		// Get the word from the tuple
		String word = input.getString(0);

		if(StringUtils.isBlank(word)){
			// ignore blank lines
			return;
		}

		System.out.println("Received Word:" + word);

		// add word to current list of words
		words.add(word);

		if (word.endsWith(".")) {
			// word ends with '.' which means this is the end of
			// the sentence publishes a sentence tuple
			collector.emit(ImmutableList.of(
					(Object) StringUtils.join(words, ' ')));

			// and reset the words list.
			words.clear();
		}
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// here we declare we will be emitting tuples with
		// a single field called "sentence"
		declarer.declare(new Fields("sentence"));
	}
}
