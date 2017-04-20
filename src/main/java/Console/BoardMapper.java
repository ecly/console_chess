package Console;

class BoardMapper {

    public BoardMapper(){

    }

    public int map(int val){
        switch(val){
            case 1: return 7;
            case 2: return 6;
            case 3: return 5;
            case 4: return 4;
            case 5: return 3;
            case 6: return 2;
            case 7: return 1;
            case 8: return 0;
        }
        return -1;
    }

    public int map(char val){
        switch(Character.toLowerCase(val)){
            case 'a': return 0;
            case 'b': return 1;
            case 'c': return 2;
            case 'd': return 3;
            case 'e': return 4;
            case 'f': return 5;
            case 'g': return 6;
            case 'h': return 7;
        }
        return -1;
    }
}