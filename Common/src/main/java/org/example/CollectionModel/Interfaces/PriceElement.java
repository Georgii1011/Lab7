package org.example.CollectionModel.Interfaces;

/**
 * Функциональный интерфейс для определения стоимости объекта
 * @param <T>
 * @param <N>
 * @param <S>
 * @param <V>
 */
@FunctionalInterface
public interface PriceElement<T, N, S, V> {
    float getPriceElement(T x, N y, S impactSpeed, V realHero);
}
