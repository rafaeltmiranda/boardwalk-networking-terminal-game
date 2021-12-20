package org.academiadecodigo.whiledlings.boardwalk.phrases;

public enum Sentences {

    ENCAPSULATION("ENCAPSULATION CAN BE USED TO HIDE DATA MEMBERS AND MEMBER FUNCTIONS"),
    INTERFACE("AN INTERFACE BASED PROGRAMMING IS ALWAYS A GOOD IDEA"),
    ABSTRACT("ABSTRACT CLASSES ARE CONCEPTUAL CLASSES THAT CANNOT BE INSTANTIATED"),
    TRANSISTOR("TRANSISTORS ARE SEMICONDUCTOR DEVICES USED TO AMPLIFY ANALOG SIGNALS"),
    VCS("VERSION CONTROL SYSTEMS MANAGE CHANGES IN DOCUMENTS"),
    VALUE("JAVA IS ONLY PASS BY VALUE"),
    COMPOSITION("COMPOSITION ALLOWS US TO DEFINE A HAS A RELATIONSHIP BETWEEN TWO OBJECTS"),
    DESIGN("DESIGN PATTERNS ARE GENERAL REPEATABLE SOLUTIONS TO COMMONLY OCCURRING PROBLEMS"),
    POLYMORPHISM("POLYMORPHISM ALLOWS INSTANCES FROM THE SAME TYPE TO HAVE DIFFERENT BEHAVIOURS"),
    MODIFIERS("PRIVATE DEFAULT PROTECTED AND PUBLIC ARE ACCESS CONTROL MODIFIERS"),
    HASH("HASH FUNCTION IS A FUNCTION THAT MAPS DATA OF ARBITRARY SIZE TO DATA OF FIXED SIZE"),
    MAP("A MAP IS AN OBJECT THAT MAPS KEYS TO VALUES"),
    STATIC("IN A STATIC METHOD YOU CAN ONLY ACCESS TO STATIC PROPERTIES OF THE CLASS"),
    LOCKS("LOCKS CAN BE USED TO SOLVE VISIBILITY ATOMICITY AND ORDERING ERRORS"),
    IP("IP ADDRESSES ENCODE BOTH A NETWORK AND A HOST ON THAT NETWORK"),
    VIM("VIM IS A POWERFUL AND EFFICIENT EDITOR AND IS BETTER THAN EMACS"),
    JAVA("JAVA IS AN OBJECT ORIENTED PROGRAMMING LANGUAGE ORIGINALLY DEVELOPED AT SUN MICROSYSTEMS"),
    TYPE("JAVA IS A STATICALLY TYPED LANGUAGE"),
    ARRAY("ARRAY IS A CONTAINER OBJECT THAT HOLDS A FIXED NUMBER OF VALUES OF A SINGLE TYPE"),
    CONSTRUCTOR("A CONSTRUCTOR IS A METHOD CALLED WHENEVER WE CREATE A NEW OBJECT"),
    BATATA("BATATA IS THE ANSWER FOR EVERY SINGLE QUESTION YOU CAN THINK OF"),
    JAVADOC("JAVADOC IS A TOOL FOR GENERATING API DOCUMENTATION IN HTML FORMAT"),
    PILLARS("THE FOUR PILLARS OF OOP ARE ENCAPSULATION ABSTRACTION INHERITANCE AND POLYMORPHISM"),
    FOWLER("GOOD PROGRAMMERS WRITE CODE THAT HUMANS CAN UNDERSTAND"),
    STRATEGY("STRATEGY IS BEHAVIORAL DESIGN PATTERN THAT ENABLES SELECTING AN ALGORITHM AT RUN TIME"),
    ENUM("ENUM IS A CLASS TYPE THAT PRE DETERMINES THE POSSIBLE VALUES FOR A VARIABLE");


    private String sentence;

    Sentences(String phrase){
        this.sentence = phrase;
    }

    public String getSentence() {
        return sentence;
    }

    public char[] getSentenceAsCharArray(){

        return sentence.toCharArray();
    }
}