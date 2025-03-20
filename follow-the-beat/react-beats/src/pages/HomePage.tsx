const HomePage = () => {
    return (
        <>
            <div className="flex flex-col w-full h-full items-center justify-center bg-gray-500">
                <h1 className="text-5xl w-full items-center text-center p-5" > Find events near you </h1>
                <h1 className="text-xl text-center p-5">Browse more then 10000 events in any location</h1>
                <input placeholder="Search events in any city..." className="w-1/2 text-center border p-2 rounded-full"></input>
            </div>
        </>
    )
}

export default HomePage;