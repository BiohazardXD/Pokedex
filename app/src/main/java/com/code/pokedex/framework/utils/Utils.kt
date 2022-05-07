package com.code.pokedex.framework.utils

import com.code.pokedex.framework.source.remote.model.Chain

class Utils {

    companion object {
        fun getPokemonId(url: String): Int {
            val parts = url.split("/")
            return parts.get(parts.size - 2).toInt()
        }


        fun formatId(id: Int): String = when (id.toString().length) {
            1 -> "00${id}"
            2 -> "0${id}"
            else -> id.toString()
        }

        fun getEvolutions(chain: Chain): List<Int> {
            val evolutions: MutableList<Int> = mutableListOf()
            getEvolutionsAux(chain, evolutions)
            return evolutions
        }

        private fun getEvolutionsAux(chain: Chain, evolutions: MutableList<Int>) {
            evolutions.add(getPokemonId(chain.species.url))
            if (chain.evolves_to.isEmpty()) {
                return
            } else {
                chain.evolves_to.map {
                    getEvolutionsAux(it, evolutions)
                }
            }
        }
    }
}