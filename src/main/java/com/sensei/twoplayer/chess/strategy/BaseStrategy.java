package com.sensei.twoplayer.chess.strategy ;

import com.sensei.twoplayer.core.EvaluationPolicy ;

public abstract class BaseStrategy implements EvaluationPolicy {
    
    private double weight = 0 ;

    public BaseStrategy( double wt ) {
        this.weight = wt ;
    }

    public double getEvaluationWeight() {
        return weight ;
    }
}
