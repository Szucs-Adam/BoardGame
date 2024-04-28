[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-24ddc0f5d75046c5622901739e7c5dd533143b0c8e959d652212380cedb1ea36.svg)](https://classroom.github.com/a/1_hdBt_5)
# TODO: Korongjáték


#Projekt leírása

A projekt célja, hogy egy olyan logikai játékot valósítsak, 
amely implementálja a leírt játék szabályait. 
A játékban két játékos vesz részt, akik felváltva lépnek.
A játéktáblán 6x6 mező található,
és az egyik játékosnak 2 piros, míg a másik játékosnak 2 
kék korongja van.

A játékosoknak két lehetőségük van lépni:

-Egy saját korongot a nyolcszomszédos üres mezőre helyezhetnek.

-Vagy maximum két mezővel elmozdíthatnak egy saját korongot, ha így az üres mezőre kerül.

A korong függőlegesen, vízszintesen és átlósan is mozoghat, át lehet ugrani közben bármelyik korongot.

Miután a játékos lehelyezett vagy áthelyezett egy korongot, annak nyolcszomszédságában az ellenfél összes korongját sajátra kell cserélni. Nem lehet továbbá a korongokkal a sötét mezőre lépni.

A játék akkor ér véget, ha a lépni következő játékos nem tud lépni. A nyerő az, akinek több saját korongja van a táblán (az üres mezők számát hozzá kell adni az utoljára lépni tudó játékos korongjainak számához).