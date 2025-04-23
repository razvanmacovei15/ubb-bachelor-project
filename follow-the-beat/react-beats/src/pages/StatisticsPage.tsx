import React from "react";
import { useLineup } from "../contexts/LineupContext";
import {
  PieChart,
  Pie,
  Cell,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from "recharts";
import { useSimulateLineupChanges } from "../hooks/useSimulateLineupChanges";

const COLORS = ["#cd7f32", "#c0c0c0", "#FFD700"];

const StatisticsPage: React.FC = () => {
  const { lineup } = useLineup();
  const { isRunning, toggle } = useSimulateLineupChanges(); 

  const tierCounts = {
    bronze: 0,
    silver: 0,
    gold: 0,
  };

  lineup.forEach((concert) => {
    if (concert.compatibility <= 33) tierCounts.bronze += 1;
    else if (concert.compatibility <= 66) tierCounts.silver += 1;
    else tierCounts.gold += 1;
  });

  const data = [
    { name: "Bronze", value: tierCounts.bronze },
    { name: "Silver", value: tierCounts.silver },
    { name: "Gold", value: tierCounts.gold },
  ];

  return (
    <div className="p-8 max-w-2xl mx-auto w-full">
      <h1 className="text-3xl font-semibold mb-8 text-center">Live Stats</h1>
      <div className="flex justify-center mb-6">
        <button
          onClick={toggle}
          className={`px-4 py-2 rounded text-white ${
            isRunning ? "bg-red-600 hover:bg-red-700" : "bg-green-600 hover:bg-green-700"
          }`}
        >
          {isRunning ? "Stop Simulation" : "Start Simulation"}
        </button>
      </div>

      <ResponsiveContainer width="100%" height={300}>
        <PieChart>
          <Pie
            data={data}
            dataKey="value"
            nameKey="name"
            cx="50%"
            cy="50%"
            outerRadius={100}
            fill="#8884d8"
            label
          >
            {data.map((_, index) => (
              <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
            ))}
          </Pie>
          <Tooltip />
          <Legend verticalAlign="bottom" height={36} />
        </PieChart>
      </ResponsiveContainer>
    </div>
  );
};

export default StatisticsPage;
