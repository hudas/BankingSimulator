# IOTSimulator

Aplikacija - simuliatorius.

Paleidimas:
java -jar simulator.jar [volt|mongo|postgres] hostname [def|run]

  def - užpildo duomenų bazę pradiniais duomenimis
  run - vykdo simuliaciją.

pvz:
java -jar simulator.jar volt localhost run


Aplikacija geba prisijungti prie vienos iš DBVS VoltDB, MongoDB, PostgreSQL ir vykdyti operacijas.

Vykdymo aplinka nurodoma config.properties faile.
Galima konfigūruoti: operacijų santykį, naudojamų gijų kiekį, darbinių įteracijų kiekį ir kitus parametrus.

Paskirtis:
Naudojantis šiuo simuliatoriumi atlikti pralaidumo matavimai.
Pradėjus simuliacija praneša apie darbo pradžią, ir darbo pabaigą.
Iš to sužinome laiką per kurį buvo atliktas iš anksto apsibrėžtas operacijų kiekis.
