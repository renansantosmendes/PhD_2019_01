package com.javacodegeeks.examples.wordcount;

import java.io.IOException;
import java.util.Iterator;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class ReduceClass extends Reducer {

    protected void reduce(Text key, Iterable values,
            Context context)
            throws IOException, InterruptedException {

        int sum = 0;
        Iterator valuesIt = values.iterator();

        while (valuesIt.hasNext()) {
            sum = sum + Integer.valueOf(valuesIt.next().toString());
        }

        context.write(key, new IntWritable(sum));
    }
}
