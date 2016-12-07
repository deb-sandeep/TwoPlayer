package com.sensei.twoplayer.core ;

/**
 * This interface should be implemented by all the game specific evaluation
 * policies. A evaluation policy is a class which encapsulates the logic of
 * evaluating a give game board in a given state with respecte to the
 * perspective provided. The evaluation value is a measure of the "goodness" of
 * the gameboard. Goodness can be defined in simple terms as 'how good is this
 * state for the specified perspective'. Goodness is measured in positive scale.
 * <p>
 * Also every evaluation policy has a weight associated with it. A weight is a
 * measure of the importance of this evaluation policy. The effect of evaluation
 * weights will be considered when more than one evaluation policy is registered
 * with the DecisionEngine. In this case the complete evaluation of the
 * gameboard at a given state would be the weighted sum of all the individual
 * evaluation values.
 */
public interface EvaluationPolicy {
    
    public double getEvaluation( GameBoard gameBoard, int perspective ) ;
    public double getEvaluationWeight() ;
}
