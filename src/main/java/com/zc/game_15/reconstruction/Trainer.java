package com.zc.game_15.reconstruction;

import com.zc.utils.SerializedObjectSaver;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static com.zc.utils.Utils.prnt;

public class Trainer {

    public static void train(HashMap<Integer, QTableRow> qTable, String filePath, int n) {
        Random random = new Random();

        double discount = 0.9d;
        double learning_rate = 0.1d;

        List<StateProducer> lessons = StateProducer.generateLessons();

        BiConsumer<StateProducer, Integer> episodeRunner = (stateProducer, episode) -> EpisodeRunner.runEpisode(stateProducer, qTable, random, discount, learning_rate, episode);
        Consumer<StateProducer> stateProducerConsumer = stateProducer -> IntStream.range(0, stateProducer.getEpisodesToTrain()).boxed().forEach(episode -> episodeRunner.accept(stateProducer, episode));

//        stateProducerConsumer.accept(lessons.get(0));
//        stateProducerConsumer.accept(lessons.get(1));
        IntStream.range(0, n).forEach(e -> lessons.forEach(stateProducerConsumer));

        prnt("\ntraining done");
        SerializedObjectSaver.save(filePath, qTable);
    }
}
