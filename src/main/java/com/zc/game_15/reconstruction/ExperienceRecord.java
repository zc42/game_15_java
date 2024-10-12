package com.zc.game_15.reconstruction;

import lombok.Getter;

import java.util.Objects;

@Getter
public class ExperienceRecord {
    private final EnvironmentState state;
    private final Action action;
    private final double reward;
    private final boolean done;
    private final EnvironmentState new_state;

    public ExperienceRecord(EnvironmentState state, Action action, double reward, boolean done, EnvironmentState new_state) {
        this.state = new EnvironmentState(state);
        this.action = action;
        this.reward = reward;
        this.done = done;
        this.new_state = new EnvironmentState(new_state);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExperienceRecord that = (ExperienceRecord) o;
        int h0 = hashCode();
        int h1 = that.hashCode();
        return h0 == h1;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state.getHashCodeV2(), new_state.getHashCodeV2());
    }
}
