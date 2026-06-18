package io.github.harsh;
import java.util.*;
public class Tokenizer {
    public List<Token> getToken(String input){
        List<Token> ans=new ArrayList<>();
        StringBuilder word= new StringBuilder();
        int i=0;
        while(i<input.length()){
            if(input.charAt(i) == '('){
                Token lparen=new Token(TokenType.LPAREN,"(");
                ans.add(lparen);
                i++;
            }
            else if(input.charAt(i) == ')'){
                Token rparen=new Token(TokenType.RPAREN,")");
                ans.add(rparen);
                i++;
            }
            else if(input.charAt(i) == ' '){
                i++;
                continue;
            }
            else{
                while(i<input.length() && input.charAt(i) != ' ' && input.charAt(i) != ')' && input.charAt(i) != '('){
                    char c= input.charAt(i);
                    word.append(c);
                    i++;
                }
                if(word.toString().equalsIgnoreCase("(")){
                    Token lparen=new Token(TokenType.LPAREN,"(");
                    ans.add(lparen);
                }
                else if(word.toString().equalsIgnoreCase(")")){
                    Token rparen=new Token(TokenType.RPAREN,")");
                    ans.add(rparen);
                }
                else if(word.toString().equalsIgnoreCase("and")){
                    Token opToken=new Token(TokenType.AND,word.toString());
                    ans.add(opToken);
                }
                else if(word.toString().equalsIgnoreCase("or")){
                    Token opToken=new Token(TokenType.OR,word.toString());
                    ans.add(opToken);
                }
                else if(word.toString().equalsIgnoreCase("nand")){
                    Token opToken=new Token(TokenType.NAND,word.toString());
                    ans.add(opToken);
                }
                else if(word.toString().equalsIgnoreCase("xor")){
                    Token opToken=new Token(TokenType.XOR,word.toString());
                    ans.add(opToken);
                }
                else if(word.toString().equalsIgnoreCase("not")){
                    Token opToken=new Token(TokenType.NOT,word.toString());
                    ans.add(opToken);
                }
                else if(word.toString().equalsIgnoreCase("xnor")){
                    Token opToken=new Token(TokenType.XNOR,word.toString());
                    ans.add(opToken);
                } 
                else{
                    Token opToken=new Token(TokenType.IDENTIFIER,word.toString());
                    ans.add(opToken);
                }
            
            }
            word.setLength(0);
        }
        ans.add(new Token(TokenType.EOF, "EOF"));
        return ans;
    }
} 