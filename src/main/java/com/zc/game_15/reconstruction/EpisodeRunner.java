package com.zc.game_15.reconstruction;

import java.text.MessageFormat;
import java.util.*;

import static com.zc.utils.RandomComparator.random;
import static com.zc.utils.Utils.prnt;
import static com.zc.utils.Utils.prntln;

public class EpisodeRunner {
    private static Set<ExperienceRecord> experience = new HashSet<>();

    public static void runEpisode
            (
                    StateProducer stateProducer,
                    Map<Integer, QTableRow> qTable,
                    Random random, double discount, double learning_rate, Integer episode
            ) {

        Environment environment = new Environment(stateProducer);

        environment.reset();
        EnvironmentState state0 = environment.getInitState();
        environment.prntInfo();

        double epsilon = 0.5d;
        boolean isTerminal = false;
        int step = 0;
        while (!isTerminal && step < 50) {
            step++;
            Action action;

            List<Action> possibleActions = Environment.getPossibleActions(state0);
            possibleActions.remove(environment.getReverseAction());
            if (random.nextDouble() < epsilon) { //explore
                prntln("\nrndm move");
                action = QTableGenerator.getRandomAction(possibleActions);
            } else { //exploit
                prntln("\nqTable move");
                action = QTableGenerator.getAction(qTable, state0, environment.getReverseAction());
                if (!possibleActions.contains(action)) {
                    action = QTableGenerator.getAction(qTable, state0, environment.getReverseAction());
                }
            }

            prntln("\n--------------------------------------------------------");
            prntln("\naction: " + action);
            if (!possibleActions.contains(action)) {
//                throw new RuntimeException("!possibleActions.contains(action)");
            }

//            try {
            EnvironmentActionResult result = environment.executeAction(state0, action);
            EnvironmentState state1 = result.getState();
            environment.prntInfo();

            double reward = result.getReward();
            isTerminal = result.isTerminal();

            if (isTerminal) {
                isTerminal = true;
            }

            ExperienceRecord record = new ExperienceRecord(state0, action, reward, isTerminal, state1);
            experience.add(record);
            state0 = result.getState();

        }

        replayExperience(experience, qTable, learning_rate, discount, 1000);

        double count = qTable.values().stream().map(e -> e.qValues.size()).mapToDouble(e -> e).sum();
        String message = MessageFormat.format("Episode {0} done, states count: {1}, experience size: {2}", episode, count, experience.size());
        prnt(message);
        prnt("");
    }

    public static void replayExperience(
            Collection<ExperienceRecord> experience, Map<Integer, QTableRow> qTable,
            double learning_rate, double discount, int sampleSize
    ) {
        experience.stream()
                .sorted(random())
                .limit(sampleSize)
                .forEach(e -> QTableUpdater.updateQTable
                        (
                                qTable,
                                e.getState(),
                                e.getAction(),
                                e.getNew_state(),
                                e.getReward(),
                                e.isDone(),
                                learning_rate,
                                discount
                        )
                );
    }
}
