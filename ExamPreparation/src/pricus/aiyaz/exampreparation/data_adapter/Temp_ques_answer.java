package pricus.aiyaz.exampreparation.data_adapter;

import java.util.ArrayList;

public class Temp_ques_answer {

	public ArrayList<String> question_id;
	public ArrayList<String> question;
	public ArrayList<String> answer1;
	public ArrayList<String> answer2;
	public ArrayList<String> answer3;
	public ArrayList<String> answer4;
	public ArrayList<String> right_answer;
	public ArrayList<String> question_type;
	
	public Temp_ques_answer() {
		// TODO Auto-generated constructor stub
		question_id=new ArrayList<String>();
		question=new ArrayList<String>();
		answer1=new ArrayList<String>();
		answer2=new ArrayList<String>();
		answer3=new ArrayList<String>();
		answer4=new ArrayList<String>();
		right_answer=new ArrayList<String>();
		question_type=new ArrayList<String>();
	}
	public void set_values(String q_id,String ques,String ans1,String ans2,String ans3,String ans4,String right_ans,String ques_type)
	{
		question_id.add(q_id);
		question.add(ques);
		answer1.add(ans1);
		answer2.add(ans2);
		answer3.add(ans3);
		answer4.add(ans4);
		right_answer.add(right_ans);
		question_type.add(ques_type);
	}
	
	public String[] get_values(int i)
	{
		String[] ret_record={question_id.get(i),question.get(i),answer1.get(i),answer2.get(i),answer3.get(i),answer4.get(i),right_answer.get(i),question_type.get(i)};
		return ret_record;	
	}
	
}
