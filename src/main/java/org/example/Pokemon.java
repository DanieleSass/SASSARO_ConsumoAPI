package org.example;

import java.util.List;

public record Pokemon(int id, String name, List<Types> types) { }
