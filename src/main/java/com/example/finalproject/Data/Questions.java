package com.example.finalproject.Data;


import java.io.Serializable;

/**
 * Object Question
 * @author Mengru.Ji
 */
public class Questions implements Serializable {

    /**
     * Description of Question
     */
    private String qDescription;
    /**
     * Id of Question
     */
    private int qId;
    /**
     * Type of Question
     */
    private int qType;
    /**
     * Wrong times of Question
     */
    private int wrongTime;
    /**
     * A of Question
     */
    private String optionA;
    /**
     * B of Question
     */
    private String optionB;
    /**
     * C of Question
     */
    private String optionC;
    /**
     * D of Question
     */
    private String optionD;
    private int answer;
    private int isCollect;
    private int selectedAnswer;

    /**
     * Constructor
     */
    public Questions(){

        this.selectedAnswer = -1;
        this.isCollect = 0;
    }

    /**
     * Constructor
     * @param qId
     * @param qDescription
     * @param qType
     * @param optionA
     * @param optionB
     * @param optionC
     * @param optionD
     * @param answer
     */
    public Questions( int qId, String qDescription, int qType,String optionA, String optionB,String optionC, String optionD,int answer){
        this.qId = qId;
        this.qDescription = qDescription;
        this.qType = qType;
        this.wrongTime = 0;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
        this.selectedAnswer = -1;
        this.isCollect = 0;
        //this.questionsOptionsList = questionsOptionsList;
    }

    /**
     * Constructor
     * @param qDescription
     * @param qType
     * @param optionA
     * @param optionB
     * @param optionC
     * @param optionD
     * @param answer
     */
    public Questions( String qDescription, int qType,String optionA, String optionB,String optionC, String optionD,int answer){
        this.qDescription = qDescription;
        this.qType = qType;
        this.wrongTime = 0;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.answer = answer;
        this.selectedAnswer = -1;
        this.isCollect = 0;
        //this.questionsOptionsList = questionsOptionsList;
    }

    /**
     * Getter for Description
     * @return
     */
    public String getQDescription() {
        return qDescription;
    }

    /**
     * Setter for Description
     * @param qDescription
     */
    public void setqDescription(String qDescription) {
        this.qDescription = qDescription;
    }

    /**
     * Getter for ID
     * @return
     */
    public int getQId() {
        return qId;
    }

    /**
     * Setter for ID
     * @param qId
     */
    public void setQId(int qId) {
        this.qId = qId;
    }

    /**
     * Getter for Type
     * @return
     */
    public int getQType() {
        return qType;
    }

    /**
     * Setter for Type
     * @param qType
     */
    public void setQType(int qType) {
        this.qType = qType;
    }


    /*public List<QuestionsOption> getQuestionsOptionsList() {
        return questionsOptionsList;
    }

    public void setQuestionsOptionsList(List<QuestionsOption> questionsOptionsList) {
        this.questionsOptionsList = questionsOptionsList;
    }*/


    /**
     * Getter for Wrong Time
     * @return
     */
    public int getWrongTime() {
        return wrongTime;
    }

    /**
     * Setter for Wrong Time
     * @param wrongTime
     */
    public void setWrongTime(int wrongTime) {
        this.wrongTime = wrongTime;
    }

    /**
     * Getter for A
     * @return
     */
    public String getOptionA() {
        return optionA;
    }

    /**
     * Setter for A
     * @param optionA
     */
    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    /**
     * Getter for B
     * @return
     */
    public String getOptionB() {
        return optionB;
    }

    /**
     * Setter for B
     * @param optionB
     */
    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    /**
     * Getter for C
     * @return
     */
    public String getOptionC() {
        return optionC;
    }

    /**
     * Setter for C
     * @param optionC
     */
    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    /**
     * Getter for D
     * @return
     */
    public String getOptionD() {
        return optionD;
    }

    /**
     * Setter for D
     * @param optionD
     */
    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    /**
     * Getter for Answer
     * @return
     */
    public int getAnswer() {
        return answer;
    }

    /**
     * Setter for Answer
     * @param answer
     */
    public void setAnswer(int answer) {
        this.answer = answer;
    }

    /**
     * Getter for Selected Answer
     * @return
     */
    public int getSelectedAnswer() {
        return selectedAnswer;
    }

    /**
     * Setter for selected Answer
     * @param selectedAnswer
     */
    public void setSelectedAnswer(int selectedAnswer) {
        this.selectedAnswer = selectedAnswer;
    }

    /**
     * if it is add in Favorites or not
     * @return
     */
    public int isCollect() {
        return isCollect;
    }

    /**
     * Setter for whether collected or not
     * @param collect
     */
    public void setCollect(int collect) {
        isCollect = collect;
    }

    /**
     * toString
     * @return
     */
    public String toString(){
        return "Q: "+ this.getQDescription()+ "\n" + " A."+ this.getOptionA() + "\n" + " B."+ this.getOptionB() + "\n" + " C."+ this.getOptionC() + "\n" + " D."+ this.getOptionD();
    }
}
