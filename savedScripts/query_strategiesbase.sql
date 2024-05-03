select distinct map

from pymrts_strategies
limit 100 

-- "Aztec"
-- "GardenOfWar64x64"

-- "chambers32x32"
-- "BloodBath"
-- "basesWorkers32x32A"
-- "basesWorkers24x24A"
-- "16x16"
-- "DoubleGame24x24"
-- "8x8"
-- "9x8"
-- "BWDistantResources32x32"

select distinct strategy, length(strategy) as size
from pymrts_strategies
where length(strategy) >= 100 
and map like 'GardenOfWar64x64'
order by 2
limit 30000