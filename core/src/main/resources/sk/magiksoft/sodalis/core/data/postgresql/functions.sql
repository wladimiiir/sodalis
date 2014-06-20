create or replace function remove_diacritics(varchar) returns varchar as
$$
select replace(replace(replace(replace(replace(replace(replace(
replace(replace(replace(replace(replace(replace(replace(
replace(replace(replace(replace(replace(replace(replace(
replace(replace(replace(replace(replace(replace(replace(
replace(replace(replace(replace(replace(replace(replace(
replace($1,'ŕ','r'),'ó','o'),'ĺ','l'),'ď','d'),
'ň','n'),'ä','a'),'ú','u'),'ô','o'),'é','e'),'ě','e'),'í','i'),
'á','a'),'ý','y'),'ž','z'),'ť','t'),'č','c'),'š','s'),'ľ','l'),
'Ŕ','R'),'Ó','O'),'Ĺ','L'),'Ď','D'),
'Ň','N'),'Ä','A'),'Ú','U'),'Ô','O'),'É','E'),'Ě','E'),'Í','I'),
'Á','A'),'Ý','Y'),'Ž','Z'),'Ť','T'),'Č','C'),'Š','S'),'Ľ','L');
$$ language sql;