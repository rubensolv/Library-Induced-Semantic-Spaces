select count(id), strategy
from pymrts_strategies
where map like 'basesWorkers24x24A'
and strategy like '%Train;Ranged;%'
and strategy like '%Train;Heavy;%'
and budget > 100000
group by 2



select distinct(map)
from pymrts_strategies


select distinct(strategy)
from pymrts_strategies
where map like 'basesWorkers24x24A'
--and strategy like '%Train;Ranged;%'
and learner in ('LL', 'DO')
and budget > 50000
and collected_file in (select collected_file from temp_24_2l_files TABLESAMPLE BERNOULLI (100))
limit 2500

select collected_file from temp_24_2l_files TABLESAMPLE BERNOULLI (5);

drop table temp_24_2l_files

select distinct(collected_file)
into temp_24_2l_files
from pymrts_strategies  where map like 'basesWorkers24x24A' and learner in ('LL') 


select *
from temp_24_2l_files o
inner join pymrts_strategies p on p.collected_file = o.collected_file
where p.budget = (select max(budget) from pymrts_strategies p2 where p2.collected_file = o.collected_file)


select distinct id
from pymrts_strategies
where map like 'basesWorkers24x24A'
and strategy like '%Train;Ranged;%'
and strategy like '%Train;Heavy;%'
and learner in ('LL')
and budget > 200000
group by 2




select distinct strategy, learner, budget, collected_file
from pymrts_strategies
where map like 'BWDistantResources32x32'
and learner in ('IBR','FP','LL')
order by collected_file, budget, learner desc



