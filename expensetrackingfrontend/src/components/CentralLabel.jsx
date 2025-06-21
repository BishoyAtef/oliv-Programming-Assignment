import { useEffect, useRef, useState } from "react";

const CenterLabel = ({ viewBox, total }) => {
  const { cx, cy } = viewBox;
  const totalText = `${total}K`;
  const textRef = useRef(null);
  const [textWidth, setTextWidth] = useState(0);

  useEffect(() => {
    if (textRef.current) {
      const bbox = textRef.current.getBBox();
      setTextWidth(bbox.width);
    }
  }, [total]);

  return (
    <g>
      <text  
        x={cx - (textWidth / 2)} 
        y={cy - 30} 
        textAnchor="start" 
        fill="gray"
        fontSize="18"
      >
        EGP
      </text>
      <text 
        ref={textRef}
        x={cx}
        y={cy + 25}
        textAnchor="middle"
        fill="black"
        fontSize="48"
        fontWeight="bold"
      >
        {totalText}
      </text>
    </g>
  );
};

export default CenterLabel;