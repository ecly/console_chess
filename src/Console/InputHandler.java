package Console;

import Chess.Pieces.Tuple;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputHandler {

    private final static Pattern validMove = Pattern.compile("([a-zA-Z][0-9])([-])([a-zA-Z][0-9])", Pattern.CASE_INSENSITIVE);
    private BoardMapper mapper;

    public InputHandler(){
        mapper = new BoardMapper();
    }

    private Tuple parse(String val){
        int x = mapper.map(val.charAt(0));
        int y = mapper.map(Integer.parseInt(String.valueOf(val.charAt(1))));

        return new Tuple(x, y);
    }

    public Tuple getFrom(String val){
        Matcher matcher = validMove.matcher(val);
        matcher.find();
        String coords = matcher.group(1);

        return parse(coords);
    }

    public Tuple getTo(String val){
        Matcher matcher = validMove.matcher(val);
        matcher.find();
        String coords =  matcher.group(3);

        return parse(coords);
    }

    public boolean isValid(String val){
        Matcher matcher = validMove.matcher(val);
        return matcher.matches();
    }
}
