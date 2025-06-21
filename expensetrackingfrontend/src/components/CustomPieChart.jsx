import { useState, useEffect } from "react";
import { PieChart, Pie, Cell, LabelList, Label, ResponsiveContainer } from "recharts";
import CenterLabel from "./CentralLabel";
import CustomCentralLabel from "./CustomCentralLable";
import flattenTree from "../utils/FlattenTree";

const CustomPieChart = ({pieChartNavigationPath}) => {
  const [navigationPath, setNavigationPath] = useState(pieChartNavigationPath);
  const currentNode = navigationPath[navigationPath.length - 1];

  const handleClick = (_, index) => {
    if (!currentNode || !currentNode.children || index == null || index < 0) return;
    const clicked = currentNode.children[index];

    if (!clicked.children || clicked.children.length === 0) return;

    const childrenData = flattenTree(clicked).map((item, i) => ({
      ...item,
      color: `hsl(${i * 37 % 360}, 60%, 60%)`
    }));

    const total = childrenData.reduce((acc, item) => acc + item.value, 0);
    setNavigationPath([...navigationPath, { ...clicked, children: childrenData, value: total }]);
  };

  const handleBack = () => {
    if (navigationPath.length > 1) {
      setNavigationPath(navigationPath.slice(0, -1));
    }
  };

  useEffect(() => {
    setNavigationPath(pieChartNavigationPath);
  }, [pieChartNavigationPath]);

  return (
    <div className="w-full flex flex-col items-center space-y-7">
      <div style={{ width: '100%', height: '600px'}}>
        <ResponsiveContainer width="100%" height="100%">
          <PieChart>
            <Pie
              data={currentNode?.children || []}
              dataKey="value"
              cx="50%"
              cy="50%"
              innerRadius="94%"
              outerRadius="95%"
              paddingAngle={2}
              stroke="none"
              labelLine={false}
              label={<CustomCentralLabel />} 
              onClick={handleClick}
              style={{ outline: 'none' }}
            >
              {(currentNode?.children || []).map((entry, index) => (
                <Cell key={`outer-${index}`} fill={entry.color} />
              ))}
            </Pie>
            <Pie
              data={currentNode?.children || []}
              dataKey="value"
              cx="50%"
              cy="50%"
              innerRadius="42%"
              outerRadius="75%"
              paddingAngle={2}
              labelLine={false}
              stroke="none"
              onClick={handleClick}
              style={{ outline: 'none' }}
            >
              {(currentNode?.children || []).map((entry, index) => (
                <Cell key={`cell-${index}`} fill={entry.color} />
              ))}
              <LabelList 
                dataKey="value" 
                position="inside" 
                formatter={(value) => `${value/1000}K`}
                style={{ 
                  fill: "gray",
                  fontSize: 18, 
                  fontWeight: 'bold',
                  textShadow: 'none'
                }}
              />
              <Label content={<CenterLabel total={currentNode?.value || 0} />} position="center" />
            </Pie>
          </PieChart>
        </ResponsiveContainer>
      </div>
      {currentNode && currentNode.children && (
        <div className="mt-6 rounded-2xl p-6 w-[90%] bg-white shadow-xl border border-gray-200 mb-10">
          <div className="flex justify-between items-center mb-4">
            <h2 className="text-xl font-bold text-gray-800">Breakdown of {currentNode.name.slice(1)}</h2>
            {navigationPath.length > 1 && (
              <button
                onClick={handleBack}
                className="bg-gray-200 hover:bg-gray-300 text-gray-800 px-3 py-1 rounded-md text-sm"
              >
                ← Back
              </button>
            )}
          </div>
          <ul className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-3">
            {currentNode.children.map((child, i) => (
              <li
                key={i}
                onClick={() => handleClick(null, i)}
                className="cursor-pointer bg-gray-100 hover:bg-gray-200 text-gray-700 p-3 rounded-xl transition-all duration-200"
              >
                <div className="font-semibold">{child.name.slice(1)}</div>
                <div>{child.value} EGP</div>
              </li>
            ))}
          </ul>
        </div>
      )}
    </div>
  );
};

export default CustomPieChart;