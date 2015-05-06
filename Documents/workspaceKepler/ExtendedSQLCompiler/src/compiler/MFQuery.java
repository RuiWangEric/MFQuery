package compiler;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MFQuery {
	
	private static ArrayList<String> OutputAttr = new ArrayList<String>();

	private static int numGVar;

	private static ArrayList<String> OutputGAttr = new ArrayList<String>();

	private static ArrayList<String> OutputFV = new ArrayList<String>();

	private static ArrayList<String> OutputConV = new ArrayList<String>();

	private static ArrayList<String> OutputHavingCon = new ArrayList<String>();

	public static void InputSelectAttr(String s) {
		String[] takes = s.split(", ");
		for (int i = 0; i < takes.length; i++) {
			OutputAttr.add(takes[i]);
		}
//		for (int i = 0; i < OutputAttr.size(); i++) {
//			System.out.println(OutputAttr.get(i));
//		}
	}

	public static void InputNumGroupingVar(String s) {
		numGVar = Integer.parseInt(s);
		// numGVar = Integer.valueOf(s);
//		System.out.println(numGVar);
	}

	public static void InputGroupingAttr(String s) {
		String[] takes = s.split(", ");
		for (int i = 0; i < takes.length; i++) {
			OutputGAttr.add(takes[i]);
		}
//		for (int i = 0; i < OutputGAttr.size(); i++) {
//			System.out.println(OutputGAttr.get(i));
//		}
	}

	public static void InputFVect(String s) {
		String[] takes = s.split(", ");
		for (int i = 0; i < takes.length; i++) {
			OutputFV.add(takes[i]);
		}
//		for (int i = 0; i < OutputFV.size(); i++) {
//			System.out.println(OutputFV.get(i));
//		}
	}

	public static void InputSelectConditionVect(String s) {
		String[] takes = s.split(", ");
		for (int i = 0; i < takes.length; i++) {
			OutputConV.add(takes[i]);
		}
//		for (int i = 0; i < OutputConV.size(); i++) {
//			System.out.println(OutputConV.get(i));
//		}
	}

	public static void InputHavingCondition(String s) {
		OutputHavingCon.add(s);
		// String[] takes = s.split(", ");
		// for (int i = 0; i < takes.length; i++) {
		// OutputHavingCon.add(takes[i]);
		// }
//		for (int i = 0; i < OutputHavingCon.size(); i++) {
//			System.out.println(OutputHavingCon.get(i));
//		}
	}

	public static void main(String[] args) throws FileNotFoundException {
		try {
			File file = new File("/Users/wangrui/Desktop/Study/CS/CS562wr/Project/test.txt");
			BufferedReader buf = new BufferedReader(new FileReader(file));
			String s = null;
			while ((s = buf.readLine()) != null) {
				if (s.contains("SELECT ATTRIBUTE")) {
					s = buf.readLine();
					InputSelectAttr(s);
					System.out.println();
				}
				if (s.contains("NUMBER OF GROUPING VARIABLES")) {
					s = buf.readLine();
					InputNumGroupingVar(s);
					System.out.println();
				}
				if (s.contains("GROUPING ATTRIBUTES")) {
					s = buf.readLine();
					InputGroupingAttr(s);
					System.out.println();
				}
				if (s.contains("F-VECT")) {
					s = buf.readLine();
					InputFVect(s);
					System.out.println();
				}
				if (s.contains("SELECT CONDITION-VECT")) {
					s = buf.readLine();
					InputSelectConditionVect(s);
					System.out.println();
				}
				if (s.contains("HAVING_CONDITION")) {
					s = buf.readLine();
					InputHavingCondition(s);
					System.out.println();
				}
			}
			buf.close();
		} catch (FileNotFoundException e2) {
			System.out.println();
			System.exit(-1);
		} catch (IOException e1) {
			System.out.println();
			System.exit(-1);
		}

		//
		try {
			PrintWriter out = new PrintWriter(
					new BufferedWriter(
							new FileWriter(
									"/Users/wangrui/Desktop/Study/CS/CS562wr/Project/out.java")));
			
			String s4  = "    ";
			String s8  = "        ";
			String s12 = "            ";
			String s16 = "                ";
			String s20 = "                    ";
			String s24 = "                        ";
			
			out.println("package test;");
			out.println("import java.sql.*;");
			out.println("import java.util.HashMap;");
			out.println();
			
			out.println("class Object {");
			out.println();
			
			out.print(s4 + "String ");
			for (int i = 0; i < OutputGAttr.size(); i++) {
				out.print(OutputGAttr.get(i));
				if (i != OutputGAttr.size() - 1) {
					out.print(", ");
				} else {
					out.println(";");
				}
			}
			out.println();
			
			int[] sumNum = new int[numGVar]; 
			int[] avgNum = new int[numGVar];
			int[] maxNum = new int[numGVar];
			int[] minNum = new int[numGVar];
			int[] countNum = new int[numGVar];
			String[] fVect = new String[OutputFV.size()];
			String[] fMethod = new String[3];
			int numberOfGroupVar = 0;
			
			for (int i = 0; i < OutputFV.size(); i++) {
				//1_sum_quant
				fVect[i] = OutputFV.get(i);
				//1, sum, quantity
				fMethod = fVect[i].split("_");
				//add number for specific person!!!
				numberOfGroupVar = Integer.parseInt(fMethod[0]) - 1;
				if (fMethod[1].equals("sum")) {
					sumNum[numberOfGroupVar] = 1;					
				} else if (fMethod[1].equals("avg")) {
					avgNum[numberOfGroupVar] = 1;  	 
				} else if (fMethod[1].equals("max")) {
					maxNum[numberOfGroupVar] = 1;
				} else if (fMethod[1].equals("min")) {
					minNum[numberOfGroupVar] = 1;
				} else if (fMethod[1].equals("count")) {
					countNum[numberOfGroupVar] = 1;
				}
			}
			
			for (int i = 0; i < numGVar; i++) {
				if (avgNum[i] > 0) {
					out.println(s4 + "int sum" + i + ";");
					out.println(s4 + "int num" + i + ";");
					out.println(s4 + "int avg" + i + ";");
				}
				if (sumNum[i] > 0 && avgNum[i] == 0)
					out.println(s4 + "int sum" + i + ";");
				if (maxNum[i] > 0)
					out.println(s4 + "int max" + i + ";");
				if (minNum[i] > 0)
					out.println(s4 + "int min" + i + ";");
				if (countNum[i] > 0)
					out.println(s4 + "int count" + i + ";");				
			}
			out.println();
			
			out.print(s4 + "public Object (");
			for (int i = 0; i < OutputGAttr.size(); i++) {
				out.print("String " + OutputGAttr.get(i));
				if (i != OutputGAttr.size() - 1) {
					out.print(", ");
				} else {
					out.println(") {");
				}
			}
			for (int i = 0; i < OutputGAttr.size(); i++) {
				out.println(s8 + "this." + OutputGAttr.get(i) + " = " + OutputGAttr.get(i) + ";");
			}
			for (int i = 0; i < numGVar; i++) {
				if (avgNum[i] > 0) {
					out.println(s8 + "this.sum" + i + " = 0;");
					out.println(s8 + "this.num" + i + " = 0;");
				}
				if (sumNum[i] > 0 && avgNum[i] == 0)
					out.println(s8 + "this.sum" + i + " = 0;");
				if (maxNum[i] > 0)
					out.println(s8 + "this.max" + i + " = 0;");
				if (minNum[i] > 0)
					out.println(s8 + "this.min" + i + " = 0;");
				if (countNum[i] > 0)
					out.println(s8 + "this.count" + i + " = 0;");				
			}
			out.println(s4 + "}");
			out.println();
			
			out.println("}");
			out.println();
			
			out.println("public class out {");
			out.println(s4 + "public static void main(String[] args) {");
			out.println();
			out.println(s8 + "String usr = " + "\"" + "postgres" + "\"" + ";");
			out.println(s8 + "String pwd = " + "\"" + "password" + "\"" + ";");//Change the pass word in this line
			out.println(s8 + "String url = " + "\"" + "jdbc:postgresql://localhost:5432/postgres" + "\"" + ";");
			out.println();
			
			out.println(s8 + "HashMap<String, Object> map = new HashMap<String, Object>();");
			out.println();
			
			out.println(s8 + "try {");
			out.println(s12 + "Class.forName(" +"\"" + "org.postgresql.Driver" + "\"" + ");");
			out.println(s12 + "System.out.println(" +"\"" + "Success loading Driver!" + "\"" + ");");
			out.println(s8 + "} catch (Exception e) {");
			out.println(s12 + "System.out.println(" +"\"" + "Fail loading Driver!" + "\"" + ");");
			out.println(s12 + "e.printStackTrace();");
			out.println(s8 + "}");
			out.println();
			
			out.println(s8 + "try {");
			out.println(s12 + "Connection conn = DriverManager.getConnection(url, usr, pwd);");
			out.println(s12 + "System.out.println(" +"\"" + "Success connecting server!" + "\"" + ");");
			out.println(s12 + "Statement stmt = conn.createStatement();");
			out.println(s12 + "ResultSet rs = stmt.executeQuery(" +"\"" + "SELECT * FROM Sales" + "\"" + ");");
			out.println();
			
			out.println(s12 + "while (rs.next()) {");
			out.println();
			out.print(s16 + "String key = ");
			for (int i = 0; i < OutputGAttr.size(); i++) {
				out.print("rs.getString(" + "\"" + OutputGAttr.get(i) + "\"" + ")");
				if (i != OutputGAttr.size() - 1) {
					out.print(" + ");
				} else {
					out.println(";");
				}
			}
			out.println();
			
			out.println(s16 + "if (!map.containsKey(key)) {");
			out.print(s20 + "Object object = new Object(");
			for (int i = 0; i < OutputGAttr.size(); i++) {
				out.print("rs.getString(" + "\"" + OutputGAttr.get(i) + "\"" + ")");
				if (i != OutputGAttr.size() - 1) {
					out.print(" + ");
				} else {
					out.println(");");
				}
			}
			out.println(s20 + "map.put(key, object);");
			out.println(s16 + "}");
			out.println();
			
			out.println(s16 + "Object temp = map.get(key);");
			out.println();
			
//------------------------------------------------------------------------------------------------------------------------------------------------------
			//F-VECT
			String[][] IndexfVect = new String[OutputFV.size()][3];//length is 5
			for (int i = 0; i < OutputFV.size(); i++) {
				IndexfVect[i] = OutputFV.get(i).split("_");
			}
			String[] GroupfVect = new String[numGVar];//length is 3
			for (int i = 0; i < OutputFV.size(); i++) {
				GroupfVect[Integer.parseInt(IndexfVect[i][0]) - 1] = GroupfVect[Integer.parseInt(IndexfVect[i][0]) - 1] + IndexfVect[i][1];
			}
			for (int i = 0; i < numGVar; i++) {
				GroupfVect[i] = GroupfVect[i].substring(4, GroupfVect[i].length());
//				System.out.println(GroupfVect[i]);
			}
			
//------------------------------------------------------------------------------------------------------------------------------------------------------			
			
			//SELECT CONDITION-VECT  Format as ... = '...' 
			String[] conditionVect = new String[OutputConV.size()];
			String[][] logicCondition = new String[OutputConV.size()][2];
			String[][][] equalBothSide = new String[OutputConV.size()][2][2];
			String[][][] IndexCondition = new String[OutputConV.size()][2][];
			
			for (int i = 0; i < OutputConV.size(); i++) {
				conditionVect[i] = OutputConV.get(i);
				if (conditionVect[i].contains(" and ")) {
					logicCondition[i] = conditionVect[i].split(" and ");
				} else if (conditionVect[i].contains(" or ")) {
					logicCondition[i] = conditionVect[i].split(" or ");
				} else {
					logicCondition[i][0] = conditionVect[i];
					logicCondition[i][1] = null;
				}
				for (int j = 0; j < logicCondition[0].length; j++) {
					if (logicCondition[i][j].length() > 0) {
						equalBothSide[i][j] = logicCondition[i][j].split(" = ");	
					}
					if (equalBothSide[i][j][1].contains("\'")) {// Format such as ...='...';
						equalBothSide[i][j][1] = equalBothSide[i][j][1].substring(1, equalBothSide[i][j][1].length() - 1);
					}
//					System.out.println(equalBothSide[i][j][0]);
//					System.out.println(equalBothSide[i][j][1]);
					
					if (logicCondition[i][j + 1] == null)
						break;
				}
			}
			for (int i = 0; i < OutputConV.size(); i++) {
				for (int j = 0; j < logicCondition[0].length; j++) {
					IndexCondition[i][j] = equalBothSide[i][j][0].split("_");
//					System.out.println(IndexCondition[i][j][1]);
					if (logicCondition[i][j + 1] == null)
						break;
				}
			}
			
			
			
//------------------------------------------------------------------------------------------------------------------------------------------------------
			
			//HAVING_CONDITION
			//OutputHavingCon
			String[] beforereplacement = new String[OutputHavingCon.size()];
			String[] afterreplacement = new String[OutputHavingCon.size()];
			String[][] logicHaving = new String[OutputHavingCon.size()][2];
			if (OutputHavingCon != null) {
//				String[] beforereplacement = new String[OutputHavingCon.size()];
//				String[] afterreplacement = new String[OutputHavingCon.size()];
//				String[][] logicHaving = new String[OutputHavingCon.size()][2];
				for (int i = 0; i < OutputHavingCon.size(); i++) {
					beforereplacement[i] = new String(OutputHavingCon.get(i));
					System.out.println(beforereplacement[i]);
					for (int j = 0; j < OutputFV.size(); j++) {
						if (OutputHavingCon.get(i).contains(OutputFV.get(j))) {
							String target = OutputFV.get(j);
//							System.out.println("target = " + target);
							String replacement = "temp." + IndexfVect[j][1] + (Integer.parseInt(IndexfVect[j][0]) - 1);
//							System.out.println("replacement = " + replacement);
							afterreplacement[i] = beforereplacement[i].replaceAll(target, replacement);
							beforereplacement[i] = afterreplacement[i];
//							System.out.println(afterreplacement[i]);
						}
					}
					if (afterreplacement[i].contains(" and "))
						afterreplacement[i] = beforereplacement[i].replaceAll("and", "&&");
					else if (afterreplacement[i].contains(" or "))
						afterreplacement[i] = beforereplacement[i].replaceAll("or", "||");
				}
//				for (int i = 0; i < afterreplacement.length; i++) {
//					System.out.println(afterreplacement[i]);
//				}
				for (int i = 0; i < afterreplacement.length; i++) {
					if (afterreplacement[i].contains(" and ")) {
						logicHaving[i] = afterreplacement[i].split(" and ");
					} else if (afterreplacement[i].contains(" or ")) {
						logicHaving[i] = afterreplacement[i].split(" or ");
					} else {
						logicHaving[i][0] = afterreplacement[i];
						logicHaving[i][1] = null;
					}
				}
			}
				
			
			
//			String[] beforereplacement = new String[OutputHavingCon.size()];
//			String[] afterreplacement = new String[OutputHavingCon.size()];
//			String[][] logicHaving = new String[OutputHavingCon.size()][2];
//			for (int i = 0; i < OutputHavingCon.size(); i++) {
//				beforereplacement[i] = new String(OutputHavingCon.get(i));
//				System.out.println(beforereplacement[i]);
//				for (int j = 0; j < OutputFV.size(); j++) {
//					if (OutputHavingCon.get(i).contains(OutputFV.get(j))) {
//						String target = OutputFV.get(j);
////						System.out.println("target = " + target);
//						String replacement = "temp." + IndexfVect[j][1] + (Integer.parseInt(IndexfVect[j][0]) - 1);
////						System.out.println("replacement = " + replacement);
//						afterreplacement[i] = beforereplacement[i].replaceAll(target, replacement);
//						beforereplacement[i] = afterreplacement[i];
////						System.out.println(afterreplacement[i]);
//					}
//				}
//				if (afterreplacement[i].contains(" and "))
//					afterreplacement[i] = beforereplacement[i].replaceAll("and", "&&");
//				else if (afterreplacement[i].contains(" or "))
//					afterreplacement[i] = beforereplacement[i].replaceAll("or", "||");
//			}
////			for (int i = 0; i < afterreplacement.length; i++) {
////				System.out.println(afterreplacement[i]);
////			}
//			for (int i = 0; i < afterreplacement.length; i++) {
//				if (afterreplacement[i].contains(" and ")) {
//					logicHaving[i] = afterreplacement[i].split(" and ");
//				} else if (afterreplacement[i].contains(" or ")) {
//					logicHaving[i] = afterreplacement[i].split(" or ");
//				} else {
//					logicHaving[i][0] = afterreplacement[i];
//					logicHaving[i][1] = null;
//				}
//			}
			
//------------------------------------------------------------------------------------------------------------------------------------------------------			
			
			for (int i = 0; i < OutputConV.size(); i++) {//length is 3
				if (i == 0)
					out.print(s16 + "if (");
				for (int j = 0; j < logicCondition[0].length; j++) {
					out.print("rs.getString(\"" + IndexCondition[i][j][1] + "\").equals(\"" + equalBothSide[i][j][1] + "\")");
					if (logicCondition[i][j + 1] == null)
						j++;
					if (j == logicCondition[0].length - 1)
						break;
					if (OutputConV.get(i).contains(" and "))
						out.print(" && ");
					else if (OutputConV.get(i).contains(" or "))
						out.print(" || ");
				}
				out.println(") {");
				if (GroupfVect[i].contains("avg")) {
					out.println(s20 + "temp.sum" + i + " += Integer.parseInt(rs.getString(\"quant\"));");
					out.println(s20 + "temp.num" + i + "++;");
				} else if (GroupfVect[i].contains("sum"))
					out.println(s20 + "temp.sum" + i + " += Integer.parseInt(rs.getString(\"quant\"));");
				if (GroupfVect[i].contains("count"))
					out.println(s20 + "temp.num" + i + "++;");
				if (GroupfVect[i].contains("min")) {
					out.println(s20 + "if (temp.min" + i + " > Integer.parseInt(rs.getString(\"quant\"))");
					out.println(s24 + "temp.min" + i + " = Integer.parseInt(rs.getString(\"quant\"));");
				}
				if (GroupfVect[i].contains("max")) {
					out.println(s20 + "if (temp.max" + i + " < Integer.parseInt(rs.getString(\"quant\"))");
					out.println(s24 + "temp.max" + i + " = Integer.parseInt(rs.getString(\"quant\"));");
				}
				if (i == OutputConV.size() - 1)
					out.println(s16 + "}");
				else 
					out.print(s16 + "} else if (");
			}
			out.println();
			
			out.println(s12 + "}");
			out.println();
			
			String cust = "CUSTOMER";
			String prod = "PRODUCT";
			out.print(s12 + "System.out.println(\"");
			for (int i = 0; i < OutputAttr.size(); i++) {
				if (OutputAttr.get(i).contains("cust"))
					out.print(cust + "  ");
				else if (OutputAttr.get(i).contains("prod"))
					out.print(prod + "  ");
				else if (i == OutputAttr.size() - 1)
					out.println(OutputAttr.get(i) + "\");");
				else 
					out.print(OutputAttr.get(i) + "  ");
			}
			out.print(s12 + "System.out.println(\"");
			for (int i = 0; i < OutputAttr.size(); i++) {
				if (OutputAttr.get(i).contains("cust")){
					for (int j = 0; j < cust.length(); j++)
						out.print("=");
					out.print("  ");
				} else if (OutputAttr.get(i).contains("prod")) {
					for (int j = 0; j < prod.length(); j++)
						out.print("=");
					out.print("  ");
				} else if (i == OutputAttr.size() - 1) {
					for (int j = 0; j < OutputAttr.get(i).length(); j++)
						out.print("=");
					out.println("\");");
				} else {
					for (int j = 0; j < OutputAttr.get(i).length(); j++)
						out.print("=");
					out.print("  ");
				}
			}
			
			out.println(s12 + "for (Object temp: map.values()) {");
			for (int i = 0; i < OutputConV.size(); i++) {//length is 3
				if (GroupfVect[i].contains("avg")) {
					out.println(s12 + "temp.avg" + i + " = temp.sum" + i + "/" +"temp.num" + i + ";");
				}
			}
			out.println();
			out.println(s16 + "if (" + afterreplacement[0] + ") {");
			for (int i = 0; i < OutputGAttr.size(); i++) {
				if (OutputAttr.get(i).contains("cust")) {
					out.println(s20 + "System.out.format(\"%-" + (cust.length() + 2) + "s\", temp." + OutputAttr.get(i) + ");");
				} else if (OutputAttr.get(i).contains("prod")) {
					out.println(s20 + "System.out.format(\"%-" + (prod.length() + 2) + "s\", temp." + OutputAttr.get(i) + ");");
				}
				
			}
			boolean afterString = true;
			for (int i = OutputGAttr.size(); i < OutputAttr.size(); i++) {
				out.print(s20 + "System.out.format(\"%");
				if (afterString == false)
					out.print(2 + OutputAttr.get(i).length());
				else {
					out.print(OutputAttr.get(i).length());
					afterString = false;
				}
				out.print("d\", temp.");
				if (OutputAttr.get(i).contains("sum"))
					out.println("sum" + (i - 1) + ");");
				else if (OutputAttr.get(i).contains("avg"))
					out.println("avg" + (i - 1) + ");");
				else if (OutputAttr.get(i).contains("count"))
					out.println("count" + (i - 1) + ");");
				else if (OutputAttr.get(i).contains("max"))
					out.println("max" + (i - 1) + ");");
				else if (OutputAttr.get(i).contains("min"))
					out.println("min" + (i - 1) + ");");
			}
			out.println(s20 + "System.out.println();");
			out.println(s16 + "}");
			out.println(s12 + "}");
			out.println(s8 + "} catch (SQLException e) {");
			out.println(s12 + "System.out.println(\"Connection URL or username or password errors!\");");
			out.println(s12 + "e.printStackTrace();");
			out.println(s8 + "}");
			out.println();
			out.println(s4 + "}");
			out.println("}");
			
//------------------------------------------------------------------------------------------------------------------------------------------------------			
			
			out.close();
		} catch (FileNotFoundException e2) {
			System.out.println();
			System.exit(-1);
		} catch (IOException e1) {
			System.out.println();
			System.exit(-1);
		}
	}

}
