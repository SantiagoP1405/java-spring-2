package com.aluracursos.santiagogomez.screenmatch_spring.service;

public interface IConvierteDatos {
    <T> T obtenerDatos(String json, Class<T> clase); //Retorna algo genérico
}
