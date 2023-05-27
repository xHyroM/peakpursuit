package me.xhyrom.peakpursuit.storage.structs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import me.zort.sqllib.internal.annotation.PrimaryKey;

@AllArgsConstructor
public class Votes {
    @PrimaryKey
    @Setter
    @Getter
    private int votes;
}
