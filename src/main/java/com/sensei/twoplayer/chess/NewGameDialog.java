package com.sensei.twoplayer.chess;

import java.awt.* ;
import java.awt.event.* ;

public class NewGameDialog extends Dialog implements ActionListener
{
    private CheckboxGroup colorCbg = null ;
    private CheckboxGroup difficultyCbg = null ;
    
    public static final int OK = 0 ;
    public static final int CANCEL = 1 ;
    public static final int NO_STATUS = 1 ;
    
    private int colorSelected = ChessBoard.WHITE ;
    private int difficultyLevel = 3 ;    
    private int userIntention = NO_STATUS ;
    
    public NewGameDialog( Component parent )
    {
        super( new Frame(), "New Game" ) ;
        setUpGUI() ;
        setLocationCenter( parent ) ;
    }
    
    private void setUpGUI()
    {
        setLayout( new GridLayout( 3, 1 ) ) ;
        setSize( 350, 150 ) ;
        setResizable( false ) ;
        
        add( getColorSelectionPanel() ) ;
        add( getDifficultyPanel() ) ;
        add( getButtonPanel() ) ;
    }
    
    private void setLocationCenter( Component parent )
    {
        int x = parent.getX() + ( parent.getWidth() - getWidth() )/2 ;
        int y = parent.getY() + ( parent.getHeight() - getHeight() )/2 ;
        
        setLocation( x, y ) ;
    }
    
    private Panel getColorSelectionPanel()
    {
        Panel panel = new Panel() ;
        panel.setBackground( Color.gray.brighter() ) ;
        panel.setLayout( new FlowLayout( FlowLayout.LEFT ) ) ;
        panel.add( new Label( "Piece color " ) ) ;
        colorCbg = new CheckboxGroup() ;
        
        Checkbox white = new Checkbox( "White", colorCbg,  true ) ;
        Checkbox black = new Checkbox( "Black", colorCbg, false  ) ;
        
        panel.add( white ) ;
        panel.add( black ) ;
        
        return panel ;
    }

    private Panel getDifficultyPanel()
    {
        Panel panel = new Panel() ;
        panel.setBackground( Color.gray.brighter() ) ;
        panel.setLayout( new FlowLayout( FlowLayout.LEFT ) ) ;
        panel.add( new Label( "Difficulty " ) ) ;
        difficultyCbg = new CheckboxGroup() ;
        
        Checkbox low = new Checkbox( "Low", difficultyCbg,  false ) ;
        Checkbox medium = new Checkbox( "Medium", difficultyCbg, true  ) ;
        Checkbox high = new Checkbox( "High", difficultyCbg, false  ) ;
        
        panel.add( low ) ;
        panel.add( medium ) ;
        panel.add( high ) ;
        
        return panel ;
    }

    private Panel getButtonPanel()
    {
        Panel panel = new Panel() ;
        panel.setBackground( Color.gray.brighter() ) ;
        Button b = new Button( "OK" ) ;
        b.setActionCommand( "OK" ) ;
        
        Button cancel = new Button( "CANCEL" ) ;
        cancel.setActionCommand( "CANCEL" ) ;
        
        b.addActionListener( this ) ;
        cancel.addActionListener( this ) ;
        
        panel.add( b ) ;
        panel.add( cancel ) ;
        
        return panel ;
    }    
    
    public void actionPerformed( ActionEvent e )
    {
        String actionCommand = e.getActionCommand() ;
        if( actionCommand.equals( "CANCEL" ) )
        {
            userIntention = CANCEL ;
        }
        else
        {
            userIntention = OK ;
            Checkbox colorCb = colorCbg.getSelectedCheckbox() ;
            String label = colorCb.getLabel() ;
            
            colorSelected = ( label.equals( "White" ) ) ? ChessBoard.WHITE : ChessBoard.BLACK ;
            
            Checkbox diffCb = difficultyCbg.getSelectedCheckbox() ;
            label = diffCb.getLabel() ;
            
            if( label.equals( "Low" ) )
            {
                difficultyLevel = 2 ;
            }
            else if( label.equals( "Medium" ) )
            {
                difficultyLevel = 3 ;
            }
            else if( label.equals( "High" ) )
            {
                difficultyLevel = 4 ;
            }
        } 
        setVisible( false ) ;       
    }    
    
    public int getUserIntention()
    {
        return userIntention ;
    }
    
    public int getUserColor() 
    {
        return colorSelected ;
    }
    
    public int getDifficultyLeve()
    {
        return difficultyLevel ;
    }
}
