from psycopg import AsyncConnection, IsolationLevel
from psycopg.errors import SerializationFailure
from psycopg_pool import AsyncConnectionPool
from .config import settings

pool = AsyncConnectionPool(
	settings.DB_URL,
	open=False,
	min_size=settings.DB_POOL_MIN_SIZE,
	max_size=settings.DB_POOL_MAX_SIZE,
)

async def get_tipjar(user_id: int) -> str | None:
	async with pool.connection() as conn:
		conn: AsyncConnection
		
		cur = await conn.execute(
			"SELECT xmr_address FROM tipjars WHERE user_id = %s",
			(user_id,)
		)
		
		results = await cur.fetchall()
		
		if len(results) == 0:
			return None
		else:
			return results[0][0]

async def set_tipjar(user_id: int, xmr_address: str | None):
	if xmr_address is None:
		return await unset_tipjar(user_id)
	
	# We don't really care about transaction isolation
	async with pool.connection() as conn:
		conn: AsyncConnection
		
		await conn.execute(
			"INSERT INTO tipjars VALUES (%s, %s) ON CONFLICT (user_id) DO UPDATE SET xmr_address = %s",
			(user_id, xmr_address, xmr_address)
		)

async def unset_tipjar(user_id: int):
	# We don't really care about transaction isolation
	async with pool.connection() as conn:
		conn: AsyncConnection
		
		await conn.execute(
			"DELETE FROM tipjars WHERE user_id = %s",
			(user_id,)
		)
