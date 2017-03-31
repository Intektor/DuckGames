package de.intektor.duckgames.util.charlist;

import com.google.common.collect.ImmutableList;

/**
 * @author Intektor
 */
public abstract class CharList {

    public static final CharList LETTERS;
    public static final CharList DIGITS;
    public static final CharList LETTERS_AND_DIGITS;
    public static final CharList PUNCTUATION_MARKS;
    public static final CharList SPACE;
    public static final CharList UNDERSCORE;

    static {
        LETTERS = new CharList() {
            @Override
            public boolean contains(char c) {
                return Character.isLetter(c);
            }
        };
        DIGITS = new CharList() {
            @Override
            public boolean contains(char c) {
                return Character.isDigit(c);
            }
        };
        LETTERS_AND_DIGITS = combine(LETTERS, DIGITS);
        PUNCTUATION_MARKS = create('.', ',', ':', '!', '?', ';');
        SPACE = create(' ');
        UNDERSCORE = create('_');
    }


    public static CharList combine(CharList... lists) {
        return new MultiListCharList(ImmutableList.copyOf(lists));
    }

    public abstract boolean contains(char c);

    public static CharList create(Character... chars) {
        return new ListBasedCharList(chars);
    }

    private static class ListBasedCharList extends CharList {

        private ImmutableList<Character> chars;

        ListBasedCharList(Character... cs) {
            chars = ImmutableList.copyOf(cs);
        }

        @Override
        public boolean contains(char c) {
            return chars.contains(c);
        }
    }

    private static class MultiListCharList extends CharList {

        private ImmutableList<CharList> lists;

        MultiListCharList(ImmutableList<CharList> lists) {
            this.lists = lists;
        }

        @Override
        public boolean contains(char c) {
            for (CharList list : lists) {
                if (list.contains(c)) return true;
            }
            return false;
        }
    }
}
