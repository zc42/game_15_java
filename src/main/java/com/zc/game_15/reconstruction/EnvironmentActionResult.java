package com.zc.game_15.reconstruction;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public  class EnvironmentActionResult {
    EnvironmentState state;
    Action action;
    double reward;
    boolean isTerminal;
}
