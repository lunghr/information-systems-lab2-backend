package com.lunghr.informationsystemslab1.service

import com.lunghr.informationsystemslab1.auth.model.ent.Role
import com.lunghr.informationsystemslab1.auth.services.JwtService
import com.lunghr.informationsystemslab1.auth.services.UserService
import com.lunghr.informationsystemslab1.dto.MagicCityDto
import com.lunghr.informationsystemslab1.dto.MagicCityResponseDto
import com.lunghr.informationsystemslab1.model.BookCreature
import com.lunghr.informationsystemslab1.model.BookCreatureType
import com.lunghr.informationsystemslab1.model.MagicCity
import com.lunghr.informationsystemslab1.model.exceptions.AccessDeniedException
import com.lunghr.informationsystemslab1.model.exceptions.CityAlreadyExistsException
import com.lunghr.informationsystemslab1.model.exceptions.CityNotFoundException
import com.lunghr.informationsystemslab1.model.repos.MagicCityRepository
import com.lunghr.informationsystemslab1.websocket.NotificationHandler
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MagicCityService @Autowired constructor(
    private val magicCityRepository: MagicCityRepository,
    private val jwtService: JwtService,
    private val userService: UserService,
    private val notificationHandler: NotificationHandler
) {

    fun createMagicCityObject(magicCityDto: MagicCityDto, token: String): MagicCity {
        val magicCity = magicCityRepository.findByName(magicCityDto.name)
            ?.let { throw CityAlreadyExistsException("City ${magicCityDto.name} already exists") }
            ?: magicCityRepository.save(
                MagicCity(
                    name = magicCityDto.name,
                    area = magicCityDto.area,
                    population = magicCityDto.population,
                    governor = BookCreatureType.valueOf(magicCityDto.governor),
                    capital = magicCityDto.capital,
                    populationDensity = magicCityDto.populationDensity,
                    establishedData = magicCityDto.establishedData,
                    user = userService.getUserByUsername(jwtService.getUsername(jwtService.extractToken(token)))
                )
            )

        return magicCity
    }

    fun createMagicCityResponseDtoObject(magicCity: MagicCity): MagicCityResponseDto {
        return MagicCityResponseDto(
            id = magicCity.id,
            name = magicCity.name,
            area = magicCity.area,
            population = magicCity.population,
            established = magicCity.establishedData,
            governor = magicCity.governor.toString(),
            capital = magicCity.capital,
            populationDensity = magicCity.populationDensity,
            userId = magicCity.user.getId()
        )
    }

    fun createMagicCity(magicCityDto: MagicCityDto, token: String): MagicCityResponseDto {
        val magicCity = createMagicCityObject(magicCityDto, token)
        notificationHandler.broadcast("City ${magicCity.name} has been created")
        return createMagicCityResponseDtoObject(magicCity)
    }

    fun addCreatureToCity(magicCityID: Long, token: String): MagicCity {
        return magicCityRepository.findMagicCityById(magicCityID) ?: throw CityNotFoundException("City not found")
    }

    fun getAllCreaturesInCity(id: Long): List<BookCreature> {
        return magicCityRepository.findMagicCityById(id)?.creatures ?: throw CityNotFoundException("City not found")
    }

    fun getAllMagicCities(): List<MagicCityResponseDto> {
        return magicCityRepository.findAll().map { city -> createMagicCityResponseDtoObject(city) }
    }

    fun getMagicCityById(id: Long): MagicCityResponseDto {
        return magicCityRepository.findMagicCityById(id)?.let { city -> createMagicCityResponseDtoObject(city) }
            ?: throw CityNotFoundException("City with id $id not found")
    }

    fun deleteMagicCityById(id: Long, token: String) {
        magicCityRepository.findMagicCityById(id)?.let { magicCity ->
            val authUser = jwtService.getUsername(jwtService.extractToken(token))
            if (magicCity.user.username != authUser && userService.getUserByUsername(authUser).role != Role.ROLE_ADMIN) {
                throw AccessDeniedException("You are not allowed to delete this city")
            } else if (magicCity.creatures.isNotEmpty()) {
                throw AccessDeniedException("You are not allowed to delete this city because it has creatures")
            }
            notificationHandler.broadcast("City ${magicCity.name} has been deleted")
            magicCityRepository.delete(magicCity)
        } ?: throw CityNotFoundException("City with id $id not found")
    }

    fun updateMagicCity(id: Long, magicCityDto: MagicCityDto, token: String): MagicCity {
        return magicCityRepository.findMagicCityById(id)
            ?.let { magicCity ->
                val authUser = jwtService.getUsername(jwtService.extractToken(token))
                if (magicCity.user.username != authUser && userService.getUserByUsername(authUser).role != Role.ROLE_ADMIN) {
                    throw AccessDeniedException("You are not allowed to update this city, because you are not the owner")
                }
                magicCity.name = magicCityDto.name
                magicCity.area = magicCityDto.area
                magicCity.population = magicCityDto.population
                magicCity.establishedData = magicCityDto.establishedData
                magicCity.governor = BookCreatureType.valueOf(magicCityDto.governor)
                magicCity.capital = magicCityDto.capital
                magicCity.populationDensity = magicCityDto.populationDensity
                notificationHandler.broadcast("City ${magicCity.name} has been updated")
                magicCityRepository.save(magicCity)
            } ?: throw CityNotFoundException("City with id $id not found")
    }

    fun findElfCities(): List<MagicCity> {
        return magicCityRepository.findAllByGovernor(BookCreatureType.ELF)
            ?: throw CityNotFoundException("City not found")
    }

    fun createMordor(token: String): MagicCity {
        return magicCityRepository.findByName("Mordor") ?: createMagicCityObject(
            MagicCityDto(
                name = "Mordor",
                area = 1000.0,
                population = 1000,
                governor = BookCreatureType.GOLLUM.toString(),
                capital = true,
                populationDensity = 1.0,
                establishedData = java.time.LocalDateTime.now()
            ),
            token
        )
    }

    fun addCreatureToMordor(token: String): MagicCity {
        return magicCityRepository.findByName("Mordor") ?: createMordor(token)
    }
}
